package service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Slf4j
public class LanguageModelService {

    public static ArrayList<String> listModelsName() {
        ArrayList<String> arrayList = new ArrayList<>();

        //path padrão dos modelos baixados via OLLAMA
        String userHome = System.getProperty("user.home");
        Path rootPath = Paths.get(userHome + "/.ollama/models/manifests/registry.ollama.ai/library");

        try (DirectoryStream<Path> rootStream = Files.newDirectoryStream(rootPath)) {
            for (Path dirEntry : rootStream) {
                if (Files.isDirectory(dirEntry)) {
                    try (DirectoryStream<Path> subStream = Files.newDirectoryStream(dirEntry)) {
                        for (Path fileEntry : subStream) {
                            if (Files.isRegularFile(fileEntry)) {
                                String formattedName = dirEntry.getFileName().toString() + ":" + fileEntry.getFileName().toString();
                                arrayList.add(formattedName);
                            }
                        }
                    } catch (IOException e) {
                        log.error("Erro ao acessar arquivos no diretório:", e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Erro ao listar diretórios no caminho: ", e);
        }
        return arrayList;
    }













//    public static void listOllamaModels(String directoryPath) {
//        File directory = new File(directoryPath);
//
//        if (directory.exists() && directory.isDirectory()) {
//            File[] files = directory.listFiles();
//
//            if (files != null) {
//                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
//
//                for (File file : files) {
//                    if (file.isDirectory()) {
//                        String name = file.getName();
//                        long size = calculateDirectorySize(file);
//                        String modifiedDate = sdf.format(new Date(file.lastModified()));
//
//                        System.out.println("NAME: " + name);
//                        System.out.println("SIZE: " + (size / (1024 * 1024)) + " MB");
//                        System.out.println("MODIFIED: " + modifiedDate);
//                        System.out.println();
//                    }
//                }
//            } else {
//                System.out.println("O diretório está vazio.");
//            }
//        } else {
//            System.out.println("O diretório não existe ou não é um diretório válido.");
//        }
//    }


}
