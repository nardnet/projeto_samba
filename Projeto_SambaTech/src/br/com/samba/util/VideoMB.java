package br.com.samba.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.util.json.JSONObject;

@ManagedBean(name="videoMB")
@SessionScoped
public class VideoMB {
	
	  private String url = "";
	
	  public VideoMB() {
	  	}
	  
	 
	  public String redirect(){
		  return "visualizar"; 
		  
	  }
		 
		public void carregarVideos(){
			 
		 
			    FacesContext facesContext = FacesContext.getCurrentInstance();
	            facesContext.getExternalContext().getRequest();    
	            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();    
	            OutputStream os = null;  
	            PrintWriter ObjSaida = null;
			    
			// Informa o tipo de arquivo de retorno...
		          response.setContentType("application/json");
		          
		        
		        try {
		        	 ObjSaida = response.getWriter();
		            // Faz uma requisição ao servidor do Zencoder solicitando a lista dos 5 últimos JOBS
		            URL ObjURL = new URL("https://app.zencoder.com/api/v2/jobs.json?api_key=" + (new PropertiesCredentials(VideoMB.class.getResourceAsStream("ZencoderCredenciais.properties"))).getAWSAccessKeyId() + "&per_page=5");
		            HttpURLConnection ObjURLConexao = (HttpURLConnection)ObjURL.openConnection();
		            ObjURLConexao.setRequestMethod("GET");
		            ObjURLConexao.connect();          
		     
		            
		            // deu certo a requisição
		            if (ObjURLConexao.getResponseCode() == 200)
		            {
		                // Obtém o stream retornado pela requisição
		                InputStreamReader ObjInputStream = new InputStreamReader((InputStream) ObjURLConexao.getContent());
		                BufferedReader buff = new BufferedReader(ObjInputStream);
		                String conteudoLido = "";
		                String linha;
		                
		                // Lê linha a linha e armazena na variável
		                do {
		                  linha = buff.readLine();
		                  if (linha != null) {
		                    conteudoLido += linha;
		                  }
		                } while (linha != null);
		    
		                // Escreve o JSON recebido
		                ObjSaida.print(conteudoLido);
		               
		            } 
		            else {
		                // Não deu certo. Envia um JSON de erro.
		                JSONObject ObjJSONErro = new JSONObject();
		                ObjJSONErro.put("Erro", "Erro ao recuperar lista de arquivos.");
		                ObjSaida.print(ObjJSONErro.toString());
		            } 
		        }            
		        catch (Exception ex) {
		            // Não deu certo. Envia um JSON de erro.
		            ObjSaida.print("{\"Erro\": \"Erro ao recuprar lista de arquivos.\"}");
		        }
		        finally {            
		            ObjSaida.close();
		            
		        }		

		 }
	  

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
