package controllers;

import play.api.Play;
import play.mvc.*;

import services.amazon.S3Service;
import views.html.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

public class Application extends Controller {
    private static S3Service s3 = new S3Service();
    public static final String DUMMY_ACCOUNT_ID = "DUMMY-ACCOUNT";

    static {
        s3.initialize();
    }

    public static Result index() {
        return ok(index.render(s3.getFiles(DUMMY_ACCOUNT_ID)));
    }

    public static Result uploadPhoto() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart picture = body.getFile("picture");
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
            File file = picture.getFile();
            String key = s3.upload(DUMMY_ACCOUNT_ID,file, fileName);
        } else {
            flash("error", "Missing file");
        }
        return redirect(routes.Application.index());
    }

    public static Result image(String key) throws IOException {
        if(key!=null){
//            BufferedImage image = ImageIO.read(s3.getS3InputStream(key));
//            ImageInputStream is = ImageIO.createImageInputStream(image);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(image, "png", baos);
//            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//            return ok(bais);
            return ok(s3.getS3InputStream(key));
        } else {
            return ok();
        }
    }
}