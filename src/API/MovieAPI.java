package API;

import DTO.Actor_DTO;
import DTO.MovieSearchResult_DTO;
import DTO.Movie_DTO;
import DTO.Review_DTO;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MovieAPI {
    public static List<MovieSearchResult_DTO> listMoviebyKeyword(String keyword){
        List<MovieSearchResult_DTO> movieSearchList = new ArrayList<MovieSearchResult_DTO>();
        //Encode keyword -> url (ví dụ "spider man" -> "spider+man")
        String keyword_encode= URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String search_url="https://www.imdb.com/search/title/?title="+keyword_encode+"&title_type=feature";
        //connect thẳng tới url search page của imdb
        try {
            Connection.Response res = Jsoup.connect(search_url)
                    .header("Accept-Language","en")
                    .method(Connection.Method.GET)
                    .timeout(3000)
                    .execute();
            Document doc = res.parse();
            //bóc HTML: lister-item mode-advanced -> div class chứa thông tin các phim khi search
            Elements listE = doc.select("div.lister-item");
            for(Element e : listE) {
                //Tạo đối tượng movieSearchResult đổ từng element vào
                MovieSearchResult_DTO movieSearchResult = new MovieSearchResult_DTO();
                //lister-item-image -> div class chứa poster
                Element imageDiv = e.getElementsByClass("lister-item-image").first();
                //trong div class của poster có thẻ a chứa url của phim
                String id = imageDiv.select("a").attr("href");
                //trong url của phim có ID phim nên bóc tách ra để lấy id để nếu người dùng chọn phim thì ta có id để get ra phim họ chọn
                String[] tmp = id.split("/");
                id = tmp[2];
                movieSearchResult.setID(id);
                //ta lấy url của poster từ trong thuộc tính loadlate của thẻ img
                String img = imageDiv.select("img").attr("loadlate");
                //vì poster lấy từ list search có size nhỏ nên ta chỉnh sửa lại url để ra size gốc
                if (!img.contains("/S/sash")) {
                    img = StringUtils.substringBefore(img, "@._V1_") + "@._V1_Ratio0.6762_AL_.jpg";
                }
                movieSearchResult.setImg(img);
                //lister-item-content -> div class này chứa tên phim, tên đạo diễn và diễn viên
                Element infoDiv = e.getElementsByClass("lister-item-content").first();
                //chỉ lấy tên phim trong thẻ H3
                Element titleDiv = infoDiv.getElementsByTag("h3").first();
                String title = titleDiv.select("a").text();
                movieSearchResult.setTitle(title);
                //ratings-bar -> div class này chứa đánh giá phim và các icon khác
                Element rateBox = infoDiv.getElementsByClass("ratings-bar").first();
                //Nếu phim không có rate thì để mặc định là không rate, nếu có thì rate nằm trong thuộc tính data-value
                String rate="No rating";
                if(rateBox!=null){
                    Element check = rateBox.getElementsByClass("ratings-imdb-rating").first();
                    if (check!=null)
                        rate = check.attr("data-value");
                }
                movieSearchResult.setRating(rate);
                //sau khi lấy tên phim, lấy tiếp tên đạo diễn và diễn viên ở thẻ P
                String directors_stars = infoDiv.getElementsByTag("p").get(2).text();
                //tên đạo diễn và diễn viên cách nhau bởi dấu | nên cần tách ra
                //nếu muốn bê nguyên lên luôn để hiển thị thì bỏ phần tách này cũng được
                String[] tmp2 = directors_stars.split(" \\| ");
                String directors = tmp2[0], stars;
                //có những phim sẽ không để tên diễn viên nên cần phải check
                if(tmp2.length==1){
                    stars="";
                } else stars=tmp2[1];
                movieSearchResult.setDirector(directors);
                movieSearchResult.setStars(stars);

                //Nếu phim nào không có poster, poster rỗng thì bỏ qua, ngược lại thì thêm vào
                if(!movieSearchResult.getImg().contains("/S/sash")){
                    //System.out.println(movieSearchResult.getImg());
                    movieSearchList.add(movieSearchResult);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return movieSearchList;
    }

    public static List<Review_DTO> getReviews(String movieID){
        List<Review_DTO> reviews = new ArrayList<Review_DTO>();
        String reviews_url = "https://www.imdb.com/title/"+movieID+"/reviews";
        //String reviews_url_2 = "https://www.imdb.com/title/"+movieID+"/reviews/_ajax?ref_=undefined&paginationKey=g4xolermtiqhejcxxxgs753i36t52q343mmtv4ppate6qp3ulnswic7qi46ra7j6mgfuqoxn";
        //connect thẳng tới url review page của imdb
        try {
            Connection.Response res = Jsoup.connect(reviews_url)
                    .header("Accept-Language","en")
                    .method(Connection.Method.GET)
                    .timeout(3000)
                    .execute();
            Document doc = res.parse();
            //imdb-user-review -> div class này chứa các reviews
            Elements listE = doc.select("div.imdb-user-review");
            for(Element e : listE){
                Review_DTO review = new Review_DTO();
                //display-name-date -> div class này chứa cả tên người rv và ngày gửi rv
                Element infoDiv = e.getElementsByClass("display-name-date").first();
                //display-name-link -> div class này chứa tên người rv
                String username = infoDiv.getElementsByClass("display-name-link").text();
                review.setUsername(username);
                //review-date -> div class này chứa ngày rv
                String date = infoDiv.getElementsByClass("review-date").text();
                review.setDate(date);
                //rating-other-user-rating -> div class này chứa điểm người rv đánh giá phim
                Element rateBox = e.getElementsByClass("rating-other-user-rating").first();
                //Nếu người rv không rate thì để mặc định là thông báo không rate, nếu có thì rate nằm trong thẻ span
                String rate="This user doesn't rate the movie";
                if(rateBox!=null) rate = rateBox.getElementsByTag("span").get(0).text();
                review.setRate(rate);
                //title -> div class này chứa câu rv ngắn đầu đề
                String title = e.getElementsByClass("title").text();
                review.setTitle(title);
                //content -> div class này chứa rv dài và phần helpful của rv
                Element contentBox = e.getElementsByClass("content").first();
                //Nếu người rv không viết full content thì để mặc định là rỗng,
                //nếu có thì content nằm trong div class text
                String content="";
                Element contentDiv = contentBox.getElementsByClass("text").first();
                if(contentDiv!=null) content=contentDiv.text();
                review.setContent(content);

                reviews.add(review);
            }
            return reviews;

        } catch (IOException e) {
            System.err.println(e);
        }
        return reviews;

    }

    public static String getTrailerUrl(String movieID){
        String output = "";
        String movie_url = "https://www.imdb.com/title/"+movieID+"/";
        //vì chỉ có id phim nên trước tiên connect đến page chứa thông tin phim
        try {
            Connection.Response res = Jsoup.connect(movie_url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")
                    .header("Accept-Language", "en")
                    .timeout(4000)
                    .method(Connection.Method.GET)
                    .execute();
            //System.out.println(res.body());
            Document doc = res.parse();
            //Media__SlateContainer-sc-1x98dcb-4 -> div class chứa trailer, thẻ a href trong class này chứa page của riêng trailer
            Elements trailerDiv = doc.getElementsByClass("Media__SlateContainer-sc-1x98dcb-4");
            if(trailerDiv.size()==0){
                output = "Phim không có Trailer";
            } else {
                String trailer_page_href = doc.getElementsByClass("Media__SlateContainer-sc-1x98dcb-4").first()
                        .select("a").attr("href");
                //kết hợp href với url gốc của imdb để được url của page riêng đó
                String trailer_page_url = "https://www.imdb.com" + trailer_page_href;
                //connect đến page của riêng trailer
                doc = Jsoup.connect(trailer_page_url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")
                        .header("Accept-Language", "en")
                        .get();
                //url file mp4 của trailer nằm trong script thứ 18 nên ta getbytag script và get vị trí 18
                String trailer_scrip = doc.getElementsByTag("script").get(18).toString();
                //url file mp4 của trailer nằm giữa 1 chuỗi có dạng:
                //{\"definition\":\"480p\",\"mimeType\":\"video/mp4\",\"url\":\"chỗ-này-là-link-của-trailer"}{\"definition\":\"SD\"
                //nên ta cần dùng StringUtils để tách url ra
                String trailer_url = StringUtils.substringBetween(trailer_scrip,
                        "{\\\"definition\\\":\\\"480p\\\",\\\"mimeType\\\":\\\"video/mp4\\\",\\\"url\\\":\\\"",
                        "\\\"},{\\\"definition\\\":\\\"SD\\\"");
                output = trailer_url;
            }
        } catch (IOException e){
            System.err.println(e);
        }
        return output;
    }

    public static Movie_DTO getMovie(String MovieID){
        Movie_DTO movie = new Movie_DTO();
        String API_key = "k_95jrg3qr";
        String movie_url="https://imdb-api.com/en/API/Title/"+API_key+"/"+MovieID;
        //Connect tới IMDB-API
        try {
            Connection.Response res = Jsoup.connect(movie_url)
                    .ignoreContentType(true)
                    .timeout(4000)
                    .method(Connection.Method.GET)
                    .execute();
            //tách Json
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(res.body());
            String title = (String) json.get("fullTitle");
            String image = (String) json.get("image");
            String trailer = getTrailerUrl(MovieID);
            String rating = (String) json.get("imDbRating");
            String rating_vote = (String) json.get("imDbRatingVotes");
            String tagline = (String) json.get("tagline");
            String plot = (String) json.get("plot");
            String releaseDate = (String) json.get("releaseDate");
            String runtimeMins = (String) json.get("runtimeMins");
            String directors = (String) json.get("directors");
            String writers = (String) json.get("writers");
            List<Actor_DTO> actors = new ArrayList<Actor_DTO>();
            JSONArray jsonArray = (JSONArray) json.get("actorList");
            for(Object obj : jsonArray){
                JSONObject jsonObj = (JSONObject) obj;
                Actor_DTO actor = new Actor_DTO();
                actor.setId((String)jsonObj.get("id"));
                actor.setName((String)jsonObj.get("name"));
                actor.setCharacter((String)jsonObj.get("asCharacter"));
                actor.setImage((String)jsonObj.get("image"));
                actors.add(actor);
            }
            String type = (String) json.get("type");
            String genres = (String) json.get("genres");
            String companies = (String) json.get("companies");
            String countries = (String) json.get("countries");
            String languages = (String) json.get("languages");
            String contentRating = (String) json.get("contentRating");
            String keywords = (String) json.get("keywords");
            List<Review_DTO> reviews = new ArrayList<Review_DTO>();
            reviews = getReviews(MovieID);
            movie = new Movie_DTO(MovieID, title, image, trailer, rating, rating_vote, tagline,
                    plot, releaseDate, runtimeMins, directors, writers, actors, type, genres, companies,
                    countries, languages, contentRating, keywords, reviews);
        } catch (IOException | ParseException e){
            System.err.println(e);
        }
        return movie;
    }

    public static void main(String[] args) {
        /*List<MovieSearchResult_DTO> movieSearchList = listMoviebyKeyword("goblin");
        if(movieSearchList.size()==0){
            System.out.println("Không tìm thấy kết quả nào khớp với tên phim yêu cầu");
        } else{
            for(int i=0; i<movieSearchList.size(); i++) {
                MovieSearchResult_DTO movieSearchResult = movieSearchList.get(i);
                System.out.println(i+". "+movieSearchResult.getTitle());
                System.out.println(movieSearchResult.getImg());
                System.out.println(movieSearchResult.getRating());
                System.out.println(movieSearchResult.getDirector());
                System.out.println(movieSearchResult.getStars());
                System.out.println("\n");
            }
        }*/
        //tt0100991 - id không có trailer
        //tt11204372 - id có trailer
        String movieID = "tt4618398";
        Movie_DTO movie = getMovie(movieID);
        System.out.println("ID phim: "+movie.getID());
        System.out.println("Title: "+movie.getTitle());
        System.out.println("IMDB Rating: "+movie.getImDbRating());
        System.out.println("Trailer: "+movie.getTrailer());
        System.out.println("Genres: "+movie.getGenres());
        System.out.println("Directors: "+movie.getDirectors());
        System.out.println("<<<  ActorList  >>>");
        for(int i=0; i<5;i++){
            Actor_DTO actor = movie.getActors().get(i);
            System.out.println(actor.getName() +" as "+actor.getCharacter());
        }
        System.out.println("<<<  Reviews  >>>");
        for(Review_DTO review: movie.getReviews()){
            System.out.println("User: ["+review.getUsername()+"] --- Date: "+ review.getDate());
            System.out.println("\""+review.getTitle()+"\"");
        }
    }
}