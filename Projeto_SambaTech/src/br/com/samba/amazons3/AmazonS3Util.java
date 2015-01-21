package br.com.samba.amazons3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

import com.amazonaws.AmazonClientException;
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
    private final static String S3_BUCKETNAME="sambabucket";   
  
    static{  
    	
    	   InputStream inputStream = AmazonS3Util.class.getResourceAsStream("AwsCredenciais.properties");  
           PropertiesCredentials credentials;
		try {
			credentials = new PropertiesCredentials(inputStream);
		    s3 = new AmazonS3Client(credentials);
		}catch (AmazonClientException az) {
			FacesContext.getCurrentInstance().addMessage( "", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não é possível calcular uma assinatura pedido: Não é possível calcular uma assinatura pedido: Chave vazia", null));	
		}catch (Exception et) {
			FacesContext.getCurrentInstance().addMessage( "", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não é possível calcular uma assinatura pedido: Não é possível calcular uma assinatura pedido: Chave vazia", null));	
		}
          
             
    }  
    
    
    public static void enviarStream(String nomeCompleto, InputStream file) 
            throws IOException, ServletException {
    	
    	 // Cria uma lista de PartResponse. Vai ter uma de cada para cada parte do upload enviada.
        List<PartETag> ListaPartETags = new ArrayList<PartETag>();
        
        InitiateMultipartUploadRequest ObjetoRequest = new InitiateMultipartUploadRequest(S3_BUCKETNAME, nomeCompleto);
        InitiateMultipartUploadResult ObjResponse = s3.initiateMultipartUpload(ObjetoRequest);
        
        int TamanhoBuffer = 5 * 1024 * 1024;
        
        int QuantidadeBytesLidos = 0;
        int ParteEnvio = 1;
        byte[] buffer = new byte[TamanhoBuffer];
        
        try {
        	
        	  while ((QuantidadeBytesLidos = file.read(buffer)) != -1) {
              	
              	
              	byte[] bufferTamanhoCorreto = new byte[QuantidadeBytesLidos];
                  System.arraycopy(buffer, 0, bufferTamanhoCorreto, 0, QuantidadeBytesLidos);
                  
                  InputStream ObjInputStreamEnviar = new ByteArrayInputStream(bufferTamanhoCorreto);
                  // Cria um Request para enviar esta parte do arquivo...
                  UploadPartRequest ObjUploadRequest = new UploadPartRequest();
                  ObjUploadRequest.setBucketName(S3_BUCKETNAME);
                  ObjUploadRequest.setKey(nomeCompleto);
                  ObjUploadRequest.setUploadId(ObjResponse.getUploadId());
                  ObjUploadRequest.setInputStream(ObjInputStreamEnviar);
                  ObjUploadRequest.setPartNumber(ParteEnvio);
                  ObjUploadRequest.setPartSize(QuantidadeBytesLidos);

                  // Adiciona esta parte na lista e envia ao Amazon S3
                  ListaPartETags.add(s3.uploadPart(ObjUploadRequest).getPartETag());   

                  ParteEnvio++; 
              	
              }
              CompleteMultipartUploadRequest ObjRequisicaoCompletar = new CompleteMultipartUploadRequest(S3_BUCKETNAME, nomeCompleto, ObjResponse.getUploadId(), ListaPartETags);
              s3.completeMultipartUpload(ObjRequisicaoCompletar);
			
		} catch (AmazonClientException e) {
			FacesContext.getCurrentInstance().addMessage( "", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível executar solicitação HTTP: sambabucket.s3.amazonaws.com", null));	
		}catch(Exception ex){			
			FacesContext.getCurrentInstance().addMessage( "", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro na aplicação! Entre em contato com o analista de sistemas", ex.getMessage()));	
		} 
    	
    }

}
