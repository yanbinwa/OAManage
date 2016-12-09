package com.yanbinwa.OASystem.Utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class QRCodeUtil
{
    @SuppressWarnings("unchecked")
    public static void CreateQRCode(String seed, String filePath) throws WriterException, IOException
    {
        int width = 100;   
        int height = 100;   
        String format = "png";   
        @SuppressWarnings("rawtypes")
        Hashtable hints= new Hashtable();   
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(seed, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, format, path);
    }
    
    public static void DecodeQRCode(String filePath) throws IOException, NotFoundException
    {
        BufferedImage image = ImageIO.read(new File(filePath));
        LuminanceSource source = new BufferedImageLuminanceSource(image);  
        Binarizer binarizer = new HybridBinarizer(source);  
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);  
        Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();  
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");  
        Result result = new MultiFormatReader().decode(binaryBitmap, hints);
        System.out.println(result.getText());
    }
    
    public static String getRandomQRCodeKey(int length)
    {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();     
        StringBuffer sb = new StringBuffer();     
        for (int i = 0; i < length; i++) {     
            int number = random.nextInt(base.length());     
            sb.append(base.charAt(number));     
        }     
        return sb.toString();
    }
    
    public static void main(String[] args) throws WriterException, IOException, NotFoundException
    {
        String filePath = "/Users/yanbinwa/Documents/workspace/OAManage/src/main/resources";
        String fileName = "test.jpg";
//        String seed = "wyblpwan";
        //CreateQRCode(seed, filePath);
        //这里会自动匹配二维码
        DecodeQRCode(filePath + '/' + fileName);
    }
}
