package com.springfullstackcloudapp.backend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ranji on 6/18/2017.
 */
@Service
public class S3Service {

    /* The Application Logger*/
    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    private static final String PROFILE_PICTURE_FILE_NAME = "profilePicture";

    @Value("${aws.s3.root.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Value("${image.store.tmp.folder}")
    private String tempImageStore;

    @Autowired
    private AmazonS3Client s3Client;

    public String storeProfileImage(MultipartFile uploadedFile, String username) throws IOException{
        String profileImageUrl = null;

        if(uploadedFile != null && !uploadedFile.isEmpty()){
            byte[] bytes = uploadedFile.getBytes();

            File tmpImageStoredFolder = new File(tempImageStore+ File.separatorChar+username);
            if(!tmpImageStoredFolder.exists()){
                LOG.info("Creating temporary root for s3 assets");
                tmpImageStoredFolder.mkdir();
            }

            File tmpProfileImageFile = new File(tmpImageStoredFolder.getAbsolutePath()
            + File.separatorChar+PROFILE_PICTURE_FILE_NAME+"."+
                    FilenameUtils.getExtension(uploadedFile.getOriginalFilename()));

            LOG.info("Temporary file will be saved to {}", tmpProfileImageFile.getAbsolutePath());

            try(BufferedOutputStream stream =
                        new BufferedOutputStream(
                                new FileOutputStream(new File(tmpProfileImageFile.getAbsolutePath())))) {
                stream.write(bytes);
            }


            profileImageUrl = this.storeProfileImageToS3(tmpProfileImageFile, username);

            tmpProfileImageFile.delete();
        }
        return profileImageUrl;
    }

    private String storeProfileImageToS3(File resource, String username) {
        String resourceUrl = null;
        if(!resource.exists()){
            LOG.error("The file {} doesn't exist. Throwing an exception", resource.getAbsolutePath());
            throw new IllegalArgumentException(" The file"+ resource.getAbsolutePath()+" doesnt exist");
        }

        String rootBucketUrl = this.ensureBucketExists(bucketName);

        if(null == rootBucketUrl){
            LOG.error("Bucket {} does not exist and application" +
                    "was not able to create it. The image won't be stored with profile", rootBucketUrl);
        }else{
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

            String key = username+"/"+PROFILE_PICTURE_FILE_NAME+"."+FilenameUtils.getExtension(resource.getName());

            try{
                s3Client.putObject(new PutObjectRequest(bucketName, key, resource).withAccessControlList(acl));
                resourceUrl = s3Client.getResourceUrl(bucketName, key);
            }catch(AmazonClientException ace){
                LOG.error("A client exception occurred while trying to store profile"+
                "image {} on S3. The profile image won't be stored", resource.getAbsolutePath(), ace);
            }
        }
        return resourceUrl;

    }

    /**
     * Returns the root url where bucket name is located
     * @param bucketName The bucket name
     * @return root URL where bucketname is located
     */
    private String ensureBucketExists(String bucketName) {

        String bucketUrl = null;
        try{
            if(!s3Client.doesBucketExist(bucketName)){
                LOG.info("Bucket {] doesnt exist...Creating one");
                s3Client.createBucket(bucketName);
                LOG.info("Created Bucket: {}", bucketName);
            }
            bucketUrl = s3Client.getResourceUrl(bucketName, null)+bucketName;
        }catch(AmazonClientException ace){
            LOG.error("An error occurred while connecting to S3. Will not execute action for bucket: {}"+bucketName, ace);
        }
        return bucketUrl;
    }
}
