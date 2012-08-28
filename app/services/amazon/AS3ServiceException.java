package services.amazon;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

/**
 * User ~ Date ~ Time: KonstantinG ~ 8/28/12 ~ 6:11 PM
 */
public class AS3ServiceException extends RuntimeException {
    private Throwable throwable;
    public AS3ServiceException(Throwable throwable) {
        super(throwable);
        this.throwable = throwable;
    }

    @Override
    public String getMessage() {
        String result = super.getMessage();
        if (throwable instanceof AmazonServiceException) {
            AmazonServiceException ase = (AmazonServiceException) throwable;
            result = "Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason."
                    + "Error Message:    " + ase.getMessage()
                    + "HTTP Status Code: " + ase.getStatusCode()
                    + "AWS Error Code:   " + ase.getErrorCode()
                    + "Error Type:       " + ase.getErrorType()
                    + "Request ID:       " + ase.getRequestId();

        } else if (throwable instanceof AmazonClientException) {
            result = "Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network."
                    + "Error Message: " + super.getMessage();
        }
        return result;
    }
}
