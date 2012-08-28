package models;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.apache.commons.io.FileUtils.checksumCRC32;
import static org.apache.commons.io.FileUtils.toFile;
import static org.fest.assertions.Assertions.assertThat;

/**
 *
 *
 *
 * User ~ Date ~ Time: KonstantinG ~ 8/28/12 ~ 9:45 AM
 */
public class AS3FileTest {
    @Test public void testFileSave() throws IOException {
//        AS3File file = new AS3File

        assertThat(remoteChecksum(AS3File.getURI())).isEqualTo(localChecksum("testImage.jpg"));
        //assertThat(file.getURI()).contains("amazon.com");
    }

    private long remoteChecksum(URL url) throws IOException {
        return checksumCRC32(toFile(url));
    }

    private long localChecksum(String testImage) throws IOException {
        return checksumCRC32(new File(testImage));
    }
}
