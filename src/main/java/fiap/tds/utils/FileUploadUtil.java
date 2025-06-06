// src/main/java/fiap/tds/utils/FileUploadUtil.java
package fiap.tds.utils;

import org.jboss.resteasy.reactive.multipart.FileUpload;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@ApplicationScoped
public class FileUploadUtil {

    @ConfigProperty(name = "image.base-url", defaultValue = "/uploads/report-images")
    String imageBaseUrl; // Mantém: /uploads/report-images

    // ALTERADO: Salva na pasta 'file-uploads/report-images' na raiz do projeto.
    private final Path rootLocation = Paths.get("src/main/resources/META-INF/resources/uploads/report-images");

    public FileUploadUtil() {
        try {
            Files.createDirectories(rootLocation); // Cria 'file-uploads/report-images' se não existir
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de upload: " + rootLocation.toString(), e);
        }
    }

    public String salvarImagem(FileUpload fileUpload) throws IOException {
        if (fileUpload == null || fileUpload.size() == 0) {
            return null;
        }

        String originalFileName = fileUpload.fileName();
        String fileExtension = "";
        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            fileExtension = originalFileName.substring(i);
        }
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        Path destinationFile = this.rootLocation.resolve(newFileName)
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new IOException("Tentativa de salvar arquivo fora do diretório de upload: " + originalFileName);
        }

        Files.copy(fileUpload.uploadedFile(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        // A URL retornada continua correta porque o imageBaseUrl já inclui /report-images
        return imageBaseUrl + "/" + newFileName;
    }

    public void deletarImagem(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty() || !imageUrl.startsWith(imageBaseUrl)) {
            System.err.println("Caminho da imagem inválido ou não gerenciado: " + imageUrl);
            return;
        }
        try {
            String fileName = imageUrl.substring(imageBaseUrl.length() + 1);
            Path fileToDelete = this.rootLocation.resolve(fileName).normalize().toAbsolutePath();

            if (Files.exists(fileToDelete) && !Files.isDirectory(fileToDelete)) {
                Files.delete(fileToDelete);
                System.out.println("Arquivo de imagem deletado: " + fileToDelete);
            } else {
                System.err.println("Arquivo de imagem não encontrado para deleção: " + fileToDelete + " (path completo no sistema de arquivos)");
            }
        } catch (IOException e) {
            System.err.println("Erro ao tentar deletar arquivo de imagem: " + imageUrl + " - " + e.getMessage());
        }
    }
}