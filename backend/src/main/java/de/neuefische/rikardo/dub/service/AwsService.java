package de.neuefische.rikardo.dub.service;

import org.springframework.stereotype.Service;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.Celebrity;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesRequest;
import com.amazonaws.services.rekognition.model.RecognizeCelebritiesResult;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import com.amazonaws.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.util.List;


@Service
public class AwsService {


    public String upload(MultipartFile file) throws IOException {

        InputStream resizeImageInputStream = new ByteArrayInputStream(resizeImage(file).toByteArray());

        String photo = file.getOriginalFilename();

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

        ByteBuffer imageBytes=null;
        try (InputStream inputStream = resizeImageInputStream) {
            imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        }
        catch(Exception e)
        {
            System.out.println("Failed to load file " + photo);
            System.exit(1);
        }

        RecognizeCelebritiesRequest request = new RecognizeCelebritiesRequest()
                .withImage(new Image()
                        .withBytes(imageBytes));

        System.out.println("Looking for celebrities in image " + photo + "\n");

        RecognizeCelebritiesResult result=rekognitionClient.recognizeCelebrities(request);

        //Display recognized celebrity information
        List<Celebrity> celebs=result.getCelebrityFaces();

        if (celebs.isEmpty()) {
            return "0 celebrity(s) were recognized";
        }
        if (celebs.size() > 1) {
            return "there are too many celebrity(s) in the image";
        }
        return celebs.get(0).getName();
    }

    public static ByteArrayOutputStream resizeImage(MultipartFile file) throws IOException {

        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        int newWidth = (int) (originalImage.getWidth()*0.5);
        int newHeight = (int) (originalImage.getHeight()*0.5);

        java.awt.Image resultingImage = originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_DEFAULT);

        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( outputImage, "jpg", baos );
        baos.flush();

        return baos;
    }


}