package API;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.*;

public class ImageAPI {
    public static boolean checkExtension(File f){
        String output="";
        String extension = getImgExtension(f);
        String supportType = "jpg, jpge, png, gif";
        if(!supportType.contains(extension)){
            return false;
        }
        return true;
    }

    public static String uploadImg(File f){
        String output = "";
        //encode image -> Base64 String
        String imgBase64 = "";
        try {
            byte[] byteArray = FileUtils.readFileToByteArray(f);
            imgBase64 = Base64.getEncoder().encodeToString(byteArray);
        } catch (IOException e) {
            output = "File không được rỗng";
            System.err.println(e);
        }
        //conect to API Imgur
        String apiKey = "afe8be93e60cc4a";
        String apiUrl = "https://api.imgur.com/3/image";
        try {
            Connection.Response res = Jsoup.connect(apiUrl)
                    .ignoreContentType(true)
                    .header("Authorization","Client-ID " + apiKey)
                    .data("image",imgBase64)
                    .data("type","base64")
                    .method(Connection.Method.POST)
                    .execute();
            //tách json - data -> link
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(res.body());
            JSONObject data = (JSONObject)json.get("data");
            output = (String)data.get("link");
        } catch (IOException | ParseException e) {
            output="Không hỗ trợ định dạng file hoặc file quá lớn";
            System.err.println(output);
        }
        return output;
    }

    public static String[] optimizeImg(String imgUrl, Integer qlty){
        //output[1] -> ảnh sau nén ; output[2] -> size ban đầu (KB); output[3] -> size sau nén (KB)
        String[] output = new String[3];
        //qlty = phần trăm ảnh người dùng muốn giữ lại sau khi nén (qtly = 90% -> ảnh sau khi nén còn lại 90% chất lượng)
        //qlty 0 -> 100, nếu người dùng nhập < 0 sẽ coi như là 0, nhập > 100 hoặc để trống sẽ coi như là 100
        if(qlty<0) qlty=0;
        if(qlty>100 || qlty==null) qlty=100;
        //http://api.resmush.it/ws.php?img=http://www.resmush.it/assets/images/jpg_example_original.jpg&qlty=90
        String apiUrl = "http://api.resmush.it/ws.php?img=";
        apiUrl = apiUrl + imgUrl + "&qlty="+qlty;
        //conect to API ReSmush
        try {
            Connection.Response res = Jsoup.connect(apiUrl)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            //tách json
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(res.body());
            output[0] = (String)json.get("dest");
            long src_size = (long)json.get("src_size"); long dest_size = (long)json.get("dest_size");
            output[1] = String.valueOf(src_size); output[2] = String.valueOf(dest_size);
        } catch (IOException | ParseException e) {
            output[0]="Không hỗ trợ định dạng file hoặc file quá lớn";
            System.err.println(output[0]);
        }
        return output;
    }

    public static HashMap<String,String> getAllSupportType(){
        HashMap<String, String> supportType = new HashMap<String, String>();
        String input=""; String output="";
        //jpg -> compress gif pdf pdfa png svg tiff watermark webp
        input = "jpg"; output ="compress, gif, pdf, pdfa, png, svg, tiff, watermark, webp";
        supportType.put(input,output);
        input = "jpge"; output ="jpg, pdf, pdfa, png, svg, tiff, webp";
        supportType.put(input,output);
        input = "gif"; output ="jpg, pdf, pdfa, png, svg, tiff, webp";
        supportType.put(input,output);
        input = "png"; output ="gif, jpg, pdf, pdfa, svg, tiff, watermark, webp";
        supportType.put(input,output);

        //jpg: gif watermark
        return supportType;
    }
    public static String getImgExtension(File f){
        return FilenameUtils.getExtension(f.getName()).toLowerCase();
    }
    public static String[] getConvertTypes(String extension, HashMap<String, String> supportType){
        String[] output=new String[10];
        if(supportType.get(extension)!=null){
            output=supportType.get(extension).split(", ");
        }
        return output;
    }

    public static String convertImg(String inputType, String outputType, String imgUrl){
        //bắt buộc nhập output type
        String output="";
        if(outputType==""){
            output="Chưa chọn output type, không thể chuyển đổi";
            return output;
        }
        //https://v2.convertapi.com/convert/jpg/to/png?Secret=g5C4IsKkQXOYVX2v&StoreFile=true
        String apiKey = "g5C4IsKkQXOYVX2v";
        String apiUrl = "https://v2.convertapi.com/convert/";
        apiUrl = apiUrl + inputType + "/to/" + outputType + "?Secret=" + apiKey
                + "&File="+ imgUrl +"&StoreFile=true";
        //connect to API ConvertApi.com
        try {
            Connection.Response res = Jsoup.connect(apiUrl)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .timeout(5000)
                    .execute();
            //tách json
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(res.body());
            JSONArray array = (JSONArray) json.get("Files");
            for(Object obj : array){
                JSONObject js = (JSONObject) obj;
                output = (String)js.get("Url");
            }
        } catch (IOException | ParseException e) {
            output="Không hỗ trợ định dạng file hoặc file quá lớn";
            System.err.println(output);
            System.err.println(e);
        }
        return output;
    }

    public static String editImg(String imgUrl, Integer gray, Integer w, Integer h, String resizeType){
        String output="";
        String apiKey="aovgnmjovq";
        //https://aovgnmjovq.cloudimg.io/v7/https://i.imgur.com/VYfqeOz.jpeg?grey=0&w=200&h=900&func=cover
        //API này thiếu các thông số vẫn hoạt động được nên không cần kiểm tra đầu vào có rỗng hay không
        //Đơn vị w và h nhỏ nhất là 10, nếu người dùng nhập < 10 sẽ coi như là 10
        String w_string = "&w=";
        String h_string = "&h=";
        if(w!=null || h!=null){
            if(w!=null){
                if(w<10) w=10;
                w_string += w;
            }
            if(h!=null){
                if(h<10) w=10;
                h_string += h;
            }
        }
        String apiUrl="https://" + apiKey + ".cloudimg.io/v7/" + imgUrl
                +"?grey=" + gray + w_string + h_string + "&func=" + resizeType;
        //API này chỉ cần thêm thông số vào là ra được link ảnh đã chỉnh sửa,
        //không cần connect tới API, nhưng cần connect đến để xem ảnh có tồn tại hay không
        output=apiUrl;
        try {
            Connection.Response res = Jsoup.connect(apiUrl)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
        } catch (IOException e) {
            output="Không hỗ trợ định dạng file hoặc file quá lớn";
            System.err.println(output);
        }
        return output;
    }

    public static List<String> similarImg(String imgUrl){
        List<String> similarImgs = new ArrayList<String>();
        //https://yandex.com/images/search?rpt=imageview&url=https://i.imgur.com/dBd43ZU.jpg&cbir_page=similar
        String similarImgPage="https://yandex.com/images/search?rpt=imageview&url=" + imgUrl + "&cbir_page=similar";
        try {
            Connection.Response res = Jsoup.connect(similarImgPage)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
                    .method(Connection.Method.GET)
                    .execute();
            Document doc = res.parse();
            Elements elements = doc.getElementsByTag("img");
            //hai element đầu và cuối rỗng
            for(int i=1;i<elements.size()-1;i++){
                String similarImgUrl = "https:"+elements.get(i).attr("src");
                similarImgs.add(similarImgUrl);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return similarImgs;
    }

    public static LinkedHashMap<String, Double> recognitionImg(String imgUrl){
        LinkedHashMap<String, Double> output = new LinkedHashMap<String, Double>();
        String authorization = "Basic YWNjXzk0NTU2OWNlMTIyMzE3ZDoxNGJlMWJmMzEwMDZlMDE5OTNlYTBhZTE4YTQ5M2JmOA==";
        String apiUrl = "https://api.imagga.com/v2/tags";
        apiUrl = apiUrl + "?image_url=" + imgUrl;
        //connect to API Imagaa
        try {
            Connection.Response res = Jsoup.connect(apiUrl)
                    .header("Authorization",authorization)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            //tách json - obj result -> mảng tags -> confidence và obj tag -> en
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(res.body());
            JSONObject result = (JSONObject)json.get("result");
            JSONArray tags = (JSONArray) result.get("tags");
            for(Object obj : tags){
                JSONObject js = (JSONObject)obj;
                String s = String.valueOf(js.get("confidence"));
                Double percent = Double.parseDouble(s);
                js = (JSONObject)js.get("tag");
                String tag = (String)js.get("en");
                BigDecimal bdUp = new BigDecimal(percent).setScale(2, RoundingMode.UP);
                output.put(tag, bdUp.doubleValue());
            }
        } catch (IOException | ParseException e) {
            System.err.println(e);
        }
        return output;
    }

    public static String downloadImg(String url_string, String typefileName, String path){
        try {
            URL url = new URL(url_string);
            //lấy tên ảnh
            //trường hợp đặt biệt với url similar
            String fileName, extension;
            if(url_string.contains("https://avatars.mds.yandex.net")){
                fileName = url_string.substring(url_string.indexOf("id=")+3, url_string.indexOf("-images"));
                extension = ".jpg";
            } else{
                //cắt lấy phần sau kí tự "/"
                //dùng url_string tmp để cắt không bị ảnh hưởng đến những phần bên dưới
                String url_tmp = url_string;
                fileName = url_tmp.substring(url_tmp.lastIndexOf("/")+1, url_tmp.lastIndexOf(".")-1);
                //lấy đuôi ảnh
                extension = url_string.substring(url_string.lastIndexOf("."));
            }
            //trường hợp đặt biệt với url edit
            if(url_string.contains("https://aovgnmjovq.cloudimg.io/v7/")){
                String[] split = extension.split("\\?");
                extension = split[0];
            }
            String fullfileName = fileName+"-"+typefileName+extension;
            //lấy thư mục lưu trữ là thư mực chứa ảnh mà người dùng tải lên
            String savePath = path.substring(0, path.lastIndexOf("\\")+1) + fullfileName;
            FileUtils.copyURLToFile(url, new File(savePath));
            return "Success";
        } catch (IOException e) {
            System.out.println("Có lỗi khi tải file");
        }
        return "Fail";
    }

//    public static void main(String[] args) {
//        //UPLOAD
//        File f = new File("D:\\Hoc\\LTM_TH\\LTM_doanDetai18Server\\src\\huou-cao-co.jpg");
//        if(checkExtension(f)==false){
//            System.out.println("Định dạng file không được hỗ trợ");
//        }
//        String imgUrl = uploadImg(f);
//        //String imgUrl = "https://i.imgur.com/gkRoRmS.png";
//        System.out.println("Ảnh từ máy upload lên site có url: "+imgUrl+"\n");
//
//        //NÉN
//        String[] optimizeImg = optimizeImg(imgUrl, 90);
//        System.out.println("Ảnh sau khi nén: "+optimizeImg[0]);
//        downloadImg(optimizeImg[0], "optimizeIMG",f.getAbsolutePath());
//        System.out.println("Size ảnh trước khi nén: "+optimizeImg[1]);
//        System.out.println("Size ảnh sau khi nén: "+optimizeImg[2]+"\n");
//
//        //CONVERT
//        //Load map chứa các định dạng hỗ trợ của các file, đây là dữ liệu cố định, có thể load riêng từ đầu
//        HashMap<String, String> supportType = getAllSupportType();
//        //Lấy đuôi file của ảnh
//        String extension = getImgExtension(f);
//        //Lấy ra các types được hỗ trợ chuyển đổi của ảnh,
//        String[] convertTypes = getConvertTypes(extension,supportType);
//        System.out.println("Ảnh gốc có định dạng: " + extension);
//        System.out.println("Có thể chuyển đổi thành các định dạng sau: ");
//        String outputConvertTypes="";
//        for(int i=1;i<=convertTypes.length;i++){
//            outputConvertTypes+=i+"."+convertTypes[i-1]+"   ";
//        }
//        System.out.println(outputConvertTypes);
//        //Chuyển đổi ảnh sau khi người dùng đã chọn output type
//        System.out.println("Ảnh đang được chuyển đổi, vui lòng chờ...");
//        String convertImg=convertImg(extension,"png",imgUrl);
//        System.out.println("Ảnh sau khi chuyển đổi: " + convertImg+"\n");
//        downloadImg(convertImg, "convertIMG", f.getAbsolutePath());
//
//        //EDIT
//        //Grayscale chỉ có lựa chọn có hoặc không, nếu là giá trị 0 hoặc rỗng sẽ không có grayscale,
//        //còn tất cả các giá trị khác bao gồm cả số âm sẽ là có grayscale
//        //Có 4 kiểu resize: crop, fit, bound, cover
//        String[] resizeType = new String[]{"crop", "fit", "bound", "cover"};
//        String editImg=editImg(imgUrl,1,200,300,resizeType[3]);
//        System.out.println("Ảnh sau khi chỉnh sửa: "+editImg+"\n");
//        downloadImg(editImg, "editIMG", f.getAbsolutePath());
//
//        //RECOGNITION
//        LinkedHashMap<String, Double> tag = recognitionImg(imgUrl);
//        System.out.println("Đối tượng đạt tỉ lệ nhận dạng cao nhất trong ảnh:");
//        System.out.println(" ===> " +tag.keySet().toArray()[0]+ " ---------- " + tag.values().toArray()[0] + "%");
//
//        //SIMILAR IMAGES
//        /*List<String> similarImgs = new ArrayList<String>();
//        similarImgs = similarImg(imgUrl);
//        System.out.println("Các hình ảnh tương tự: ");
//        for(int i=0;i<similarImgs.size();i++){
//            System.out.println(similarImgs.get(i));
//        }*/
//        downloadImg("https://avatars.mds.yandex.net/i?id=1f75c0f79416ceb14188978791f4ee3d-4571642-images-thumbs&n=13",
//                "similarIMG",
//                f.getAbsolutePath());
//    }
}
