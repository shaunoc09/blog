package ch.rasc.upload;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.RegionConflictException;
import io.minio.errors.XmlParserException;

@RestController
public class PreSignController {
  private final static String BUCKET_NAME = "uploads";

  private final MinioClient minioClient;

  public PreSignController(MinioClient minioClient) {
    this.minioClient = minioClient;
  }

  @PostConstruct
  public void createBucket() throws InvalidKeyException, InvalidBucketNameException,
      NoSuchAlgorithmException, InsufficientDataException, ErrorResponseException,
      InternalException, IOException, RegionConflictException, InvalidResponseException,
      IllegalArgumentException, XmlParserException {

    if (!this.minioClient.bucketExists(BUCKET_NAME)) {
      this.minioClient.makeBucket(BUCKET_NAME);
    }

  }

  @CrossOrigin
  @GetMapping("/getPreSignUrl")
  public String getPreSignUrl(@RequestParam("fileName") String fileName)
      throws InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException,
      InsufficientDataException, ErrorResponseException, InternalException,
      InvalidExpiresRangeException, IOException, InvalidResponseException,
      IllegalArgumentException, XmlParserException {

    return this.minioClient.presignedPutObject(BUCKET_NAME, fileName, 60);

  }
}
