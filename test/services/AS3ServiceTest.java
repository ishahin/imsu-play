package services;

import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.junit.Test;
import services.amazon.S3Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

import static org.apache.commons.io.FileUtils.toFile;
import static org.fest.assertions.Assertions.assertThat;

/**
 * User ~ Date ~ Time: KonstantinG ~ 8/28/12 ~ 9:45 AM
 */
public class AS3ServiceTest {
    S3Service s3service = new S3Service();

    //TODO: AHTUNG!!!! ATTENTION!!!! SHOULD BE REFACTORED TO USE TEST BUCKED!!!!
    @Test @Deprecated public void testInitTerminate() {
        cleanupBucket();
        assertThat(s3service.isInitialized()).isFalse();
        s3service.initialize();
        assertThat(s3service.isInitialized()).isTrue();
    }

    //TODO: AHTUNG!!!! ATTENTION!!!! SHOULD BE REFACTORED TO USE TEST BUCKED!!!!
    @Test @Deprecated public void testAS3ServiceCRUD() throws Exception {
        s3service.initialize();
        String accountID = "testaccountid";
        URL testImageLocalPath = getClass().getResource("testImage.jpg");
        File file = toFile(testImageLocalPath);
        String fileKey = s3service.upload(accountID, new FileInputStream(file), "testImage.jpg");
        assertThat(s3service.exists(accountID, fileKey)).isTrue();

        String publicURL = s3service.getPublicURL(fileKey);

        Checksum checksum;
        long testImageChecksum = getChecksum(new FileInputStream(toFile(testImageLocalPath)));

        InputStream objectContent = s3service.getS3InputStream(fileKey);

        assertThat(getChecksum(objectContent)).isEqualTo(testImageChecksum);

        //LIST TEST
        assertThat(s3service.getFiles(accountID).size()).isEqualTo(1);

        //REMOVE TEST
        s3service.remove(fileKey);
        assertThat(s3service.exists(accountID, fileKey)).isFalse();
    }

    //TODO: AHTUNG!!!! ATTENTION!!!! SHOULD BE REFACTORED TO USE TEST BUCKED!!!!
    private void cleanupBucket(){
        if(s3service.isInitialized()){
            ObjectListing objectListing = s3service.getS3().listObjects(new ListObjectsRequest().withBucketName(S3Service.IMSU_PLAY_BUCKET));
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                s3service.remove(objectSummary.getKey());
            }
            s3service.getS3().deleteBucket(S3Service.IMSU_PLAY_BUCKET);
        }
    }


    public static long getChecksum(InputStream is) {
        CheckedInputStream cis = null;
        long checksum = 0;
        try {
            cis = new CheckedInputStream(is, new Adler32());
            byte[] tempBuf = new byte[1024];
            while (cis.read(tempBuf) >= 0) {
            }
            checksum = cis.getChecksum().getValue();
        } catch (IOException e) {
            checksum = 0;
        } finally {
            if (cis != null) {
                try {
                    cis.close();
                } catch (IOException ioe) {
                }
            }
        }
        return checksum;
    }
}