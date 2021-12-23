package Server;

import API.ImageAPI;
import API.MovieAPI;
import DTO.MovieSearchResult_DTO;
import DTO.Movie_DTO;
import DTO.Review_DTO;
import Support.SupportTool;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Worker2 implements Runnable {
    private Socket socket;


    public Worker2(Socket s) throws IOException {
            this.socket = s;

//        SupportTool.InitializeInputStream(s);
//        SupportTool.InitializeOutputStream(s);
    }
    public void run() {
        System.out.println("Client " + socket.toString() + " accepted");
//        String input = "";
        //HashMap<String,Object> input = new HashMap<>() ;
        try {
            SupportTool.InitializeInputStream(socket);
            SupportTool.InitializeOutputStream(socket);
          //  SupportTool.getOutputStream().flush();
            HashMap<String,Object> input = new HashMap<>() ;
            while (true) {

                input = (HashMap<String,Object>) SupportTool.getInputStream().readObject();
                if (input.containsKey("1")) {
                    String tenphim = (String) input.get("1");
                    List<MovieSearchResult_DTO> list = MovieAPI.listMoviebyKeyword(tenphim.trim());
                    SupportTool.getOutputStream().writeObject(list);
                }
                if ( input.containsKey("2"))
                {
                    String titleMovie=(String) input.get("2");
                    Movie_DTO movie = MovieAPI.getMovie(titleMovie.trim().replaceAll("\\s+"," "));
                    SupportTool.getOutputStream().writeObject(movie);
                }
                if ( input.containsKey("3"))
                {
                    String titleMovie=(String) input.get("3");
                    List<Review_DTO> review_dtos = MovieAPI.getReviews(titleMovie.trim().replaceAll("\\s+"," "));
                    SupportTool.getOutputStream().writeObject(review_dtos);
                }
                if ( input.containsKey("4"))
                {
                    String titleMovie=(String) input.get("4");
                    System.out.println(titleMovie);
                    String trailerUrl = MovieAPI.getTrailerUrl(titleMovie.trim().replaceAll("\\s+"," "));
                    System.out.println(trailerUrl);
                    SupportTool.getOutputStream().writeObject(trailerUrl);
                }
                if ( input.containsKey("5"))
                {
                    File file= (File) input.get("5");
                    String imageURL = ImageAPI.uploadImg(file);
                    SupportTool.getOutputStream().writeObject(imageURL);
                }
                if ( input.containsKey("6"))
                {
                    HashMap<String,String> infoCompress = (HashMap<String, String>) input.get("6");
                    String imageURL=infoCompress.get("imgURL");
                    System.out.println("Server get" +imageURL);
                    Integer qlty = Integer.valueOf(infoCompress.get("qlty"));
                    System.out.println("Server get" +qlty);
                    List<String> infoCompressList = List.of(ImageAPI.optimizeImg(imageURL, qlty));
                    System.out.println(infoCompressList);
                    SupportTool.getOutputStream().writeObject(infoCompressList);
                }

                if ( input.containsKey("7"))
                {
                    HashMap<String,String> infoConvert = (HashMap<String, String>) input.get("7");
                    System.out.println("Server get" +infoConvert);
                    String imageURL=infoConvert.get("imgURL");
                    System.out.println("Server get " +imageURL);
                    String outputType = infoConvert.get("outputType");
                    System.out.println("Server get " +outputType);
                    String inputType = infoConvert.get("inputType");
                    System.out.println("Server get " +inputType);
                    String output = ImageAPI.convertImg(inputType,outputType,imageURL);
                    System.out.println(output);
                    SupportTool.getOutputStream().writeObject(output);
                }
                if ( input.containsKey("8"))
                {
                    HashMap<String,String> infoEdit = (HashMap<String, String>) input.get("8");
                    System.out.println("Server get" +infoEdit);
                    String imageURL=infoEdit.get("imgURL");
                    System.out.println("Server get " +imageURL);
                    Integer gray = Integer.valueOf(infoEdit.get("gray"));
                    System.out.println("Server get " +gray);
                    Integer width = Integer.valueOf(infoEdit.get("width"));
                    System.out.println("Server get " +width);
                    Integer height = Integer.valueOf(infoEdit.get("height"));
                    System.out.println("Server get " +height);
                    String resizeType=infoEdit.get("resizeType");
                    System.out.println("Server get " +resizeType);

                    String output = ImageAPI.editImg(imageURL,gray,width,height,resizeType);
                    System.out.println(output);
                    SupportTool.getOutputStream().writeObject(output);
                }
                if ( input.containsKey("9"))
                {
                    String imgUrl=(String) input.get("9");
                    List<String> similarImg=ImageAPI.similarImg(imgUrl);
                    SupportTool.getOutputStream().writeObject(similarImg);
                }
                if ( input.containsKey("10"))
                {
                    String imgUrl=(String) input.get("10");
                    LinkedHashMap<String, Double> recognitionImg=ImageAPI.recognitionImg(imgUrl);
                    SupportTool.getOutputStream().writeObject(recognitionImg);
                }
                if ( input.containsKey("11"))
                {
                    HashMap<String,String> infoSave = (HashMap<String, String>) input.get("11");
                    System.out.println("Server get" +infoSave);
                    String imageURL=infoSave.get("imgURL");
                    System.out.println("Server get " +imageURL);
                    String typeFileName = infoSave.get("typeFileName");
                    System.out.println("Server get " +typeFileName);
                    String pathFile=infoSave.get("pathFile");
                    System.out.println("Server get " +pathFile);

                    String output = ImageAPI.downloadImg(imageURL,typeFileName,pathFile);
                    System.out.println(output);
                    SupportTool.getOutputStream().writeObject(output);
                }




            }
        }
            catch(IOException e)
            {
                e.printStackTrace();
            } catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        }
        //System.out.println("Closed socket for client " + myName + " " + socket.toString());

//            socket.close();

