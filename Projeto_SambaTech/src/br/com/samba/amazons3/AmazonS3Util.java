package br.com.samba.amazons3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class AmazonS3Util {
	
	private static AmazonS3 s3;  
    private final static String S3WAWSURL = "https://s3-sa-east-1.amazonaws.com/%s/%s";  
    private final static String S3_BUCKETNAME="nome_do_bucket";   
  
    static{  
    	
    	   InputStream inputStream = AmazonS3Util.class.getResourceAsStream("AwsCredenciais.properties");  
           PropertiesCredentials credentials;
		try {
			credentials = new PropertiesCredentials(inputStream);
			 AmazonS3 s3 = new AmazonS3Client(credentials);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
             
    }  
    
    
    public static void enviarStream(String NomeArquivoExt, InputStream ObjInputArquivoUpload) 
            throws IOException, ServletException {
    	
    	 // Cria uma lista de PartResponse. Vai ter uma de cada para cada parte do upload enviada.
        List<PartETag> ListaPartETags = new ArrayList<PartETag>();
    	
    }

}
