package services.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import play.Play;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import com.typesafe.config.ConfigFactory;

public class S3Service {
    private AmazonS3 s3;

    public static final String IMSU_PLAY_BUCKET = "imsu-play-bucket";

    public S3Service() {
        try {
            String s3_key = System.getenv("S3_KEY");
            String s3_secret = System.getenv("S3_SECRET");
            if(s3_key != null && s3_secret != null){
                s3 = new AmazonS3Client(new BasicAWSCredentials(s3_key, s3_secret));
            } else {
                InputStream resourceAsStream = S3Service.class.getResourceAsStream("/aws-credentials.properties");
                s3 = new AmazonS3Client(new PropertiesCredentials(resourceAsStream));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public String upload(String accountID, File file) {
        return upload(accountID, file, file.getName());
    }

    public String upload(String accountID, File file, String name) {
        try {
            return upload(accountID, new FileInputStream(file), name);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String upload(String accountID, InputStream inputSteam, String fileName) {
        String key = generateFileKey(accountID, fileName);
        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            s3.putObject(new PutObjectRequest(IMSU_PLAY_BUCKET, key, inputSteam, new ObjectMetadata()));
        } catch (Exception e) {
            throw new AS3ServiceException(e);
        }
        return key;
    }

    public boolean exists(String accountID, String fileKey) {
        boolean result = false;
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
                .withBucketName(IMSU_PLAY_BUCKET).withPrefix(accountID));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            if (fileKey.equals(objectSummary.getKey())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean isInitialized() {
        boolean result = false;
        for (Bucket bucket : s3.listBuckets()) {
            if (IMSU_PLAY_BUCKET.equals(bucket.getName())) {
                result = true;
                break;
            }
        }
        return result;
    }

    public synchronized void initialize() {
        if (!isInitialized()) {
            s3.createBucket(IMSU_PLAY_BUCKET);
        }
    }

    public AmazonS3 getS3() {
        return s3;
    }

    /**
     * AS3 public URL should be replaced by URL to imsu....com
     */
    @Deprecated
    public String getPublicURL(String fileKey) {
        StringBuffer result = new StringBuffer("https://").append(IMSU_PLAY_BUCKET).append(".s3.amazonaws.com/").append(fileKey);
        return result.toString();
    }

    public void remove(String fileKey) {
        s3.deleteObject(IMSU_PLAY_BUCKET, fileKey);
    }

    public InputStream getS3InputStream(String fileKey) {
        S3Object object = s3.getObject(new GetObjectRequest(IMSU_PLAY_BUCKET, fileKey));
        return object.getObjectContent();
    }

    private String generateFileKey(String accountID, String fileName) {
        return accountID + "/" + fileName;
    }

    public List<String> getFiles(String accountID) {
        List<String> result = new LinkedList<String>();
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(IMSU_PLAY_BUCKET).withPrefix(accountID));
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            result.add(objectSummary.getKey());
        }
        return result;
    }
}
