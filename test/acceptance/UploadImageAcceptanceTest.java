package acceptance;

import org.junit.Test;

/**
 * Как пользователь используя утилиту IMSUploader я могу загрузить фото из директории в клауд
 * Для того чтобы не тратить время на несколько отдельных операций (выбрать файл, загрузить его в клауд и тд)
 *
 * GIVEN Указал директорию с файлами
 * WHEN Я запускаю скрипт IMSUploader
 * THEN Система загружает все файлы из директории на AmazonS3 клауд по HTTP
 * AND Возвращает JSON Документ с списком URI файлов
 *
 * User ~ Date ~ Time: KonstantinG ~ 8/28/12 ~ 9:22 AM
 */
public class UploadImageAcceptanceTest {
    @Test
    public void test(){

    }
}
