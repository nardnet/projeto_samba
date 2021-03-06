package br.com.samba.util;

import java.io.IOException;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletException;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.samba.amazons3.AmazonS3Util;
import br.com.samba.zenconder.ZencoderUtil;


@ManagedBean(name="uploadMB")
@RequestScoped
public class UploadMB {

	public UploadMB() {
	}
	
	


	//Teste
	public void doUpload(FileUploadEvent fileUploadEvent) throws IOException, ServletException{
		UploadedFile uploadedFile = fileUploadEvent.getFile();		
		String fileName = uploadedFile.getFileName();
		String extensaoArquivo=  uploadedFile.getFileName().substring(uploadedFile.getFileName().lastIndexOf(".") + 1, uploadedFile.getFileName().length());
		long fileSizeUploaded = uploadedFile.getSize();
		
		fileName += UUID.randomUUID().toString();		
		fileName = fileName.replaceAll("[^a-zA-Z0-9]+","");
				 
        String nomeCompleto = fileName + "." + extensaoArquivo;
        
               
         AmazonS3Util.enviarStream(nomeCompleto, uploadedFile.getInputstream());
         ZencoderUtil.SolicitarConversao(fileName, nomeCompleto);
         
         
		String infoAboutFile = "<br/> Arquivo recebido: <b>" +fileName +"</b><br/>"+ "Tamanho do Arquivo: <b>"+fileSizeUploaded+"</b>"
		+"</b><br/>"+ "Extens�o do Arquivo: <b>"+extensaoArquivo+"</b>";
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage("Sucesso", infoAboutFile));		
        // Redireciona o usuário para página de sucesso
       
	}
	
	
}
