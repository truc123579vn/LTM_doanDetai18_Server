//package Support;
//
//import DTO.Actor_DTO;
//import DTO.MovieSearchResult_DTO;
//import DTO.Movie_DTO;
//import DTO.Review_DTO;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.commons.lang3.StringUtils;
//
//
//public class Movie_code {
//    public static List<MovieSearchResult_DTO> listMoviebyKeyword(String keyword){
//        //keyword="avengers";
//        String keyword_encode=URLEncoder.encode(keyword, StandardCharsets.UTF_8);
//        String search_url="https://www.imdb.com/search/title/?title="+keyword_encode+"&title_type=feature,tv_movie&count=250";
//        List<MovieSearchResult_DTO> moviesSearchResult = new ArrayList<MovieSearchResult_DTO>();
//        try {
//            Document doc=Jsoup.connect(search_url)
//                    .header("Accept-Language", "en")
//                    .timeout(4000)
//                    .get();
////            Elements listE = doc.getElementsByClass("lister-item mode-advanced");
//            Elements listE = doc.select(".lister-list > div");
//            for(Element e : listE){
//                MovieSearchResult_DTO movieSearchResult = new MovieSearchResult_DTO();
//                //Element imageDiv = e.getElementsByClass("lister-item-image float-left").first();
//                String id =e.getElementsByTag("a").attr("href");
//              //  String id=imageDiv.select("a").attr("href");
//                String[] tmp = id.split("/");
//                id=tmp[2];
//                movieSearchResult.setID(id);
//
//               // String img=imageDiv.select("img").attr("loadlate");
//                String img=e.getElementsByClass("loadlate").attr("loadlate");
//               //System.out.println(img);
//                if(!img.contains("/S/sash")) {
//                    img=StringUtils.substringBefore(img,"@._V1_")+"@._V1_Ratio0.6762_AL_.jpg";
//                }
//                movieSearchResult.setImg(img);
//
//                Element infoDiv = e.getElementsByClass("lister-item-content").first();
//                String title = infoDiv.getElementsByTag("h3").text();
//                movieSearchResult.setTitle(title);
//
//                Element tmp1 = infoDiv.getElementsByClass("ratings-bar").first();
//                String rating="No rating";
//                if(tmp1!=null){
//                    Element check = tmp1.getElementsByClass("inline-block ratings-imdb-rating").first();
//                    if (check!=null)
//                        rating = tmp1.getElementsByClass("inline-block ratings-imdb-rating").attr("data-value");
//                }
//                movieSearchResult.setRating(rating);
//
//                String directors_stars = infoDiv.getElementsByTag("p").get(2).text();
//                String[] tmp2 = directors_stars.split(" \\| ");
//                String directors = tmp2[0], stars;
//                if(tmp2.length==1){
//                    stars="";
//                } else stars=tmp2[1];
//                movieSearchResult.setDirector(directors);
//                movieSearchResult.setStars(stars);
//                //System.out.println();
//                moviesSearchResult.add(movieSearchResult);
//            }
//
//            return moviesSearchResult;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return moviesSearchResult;
//    }
//
//    public static List<Review_DTO> getReviews(String movieID){
//        //movieID="tt0145487";
//        String reviews_url = "https://www.imdb.com/title/"+movieID+"/reviews";
//        String reviews_url_2 = "https://www.imdb.com/title/"+movieID+"/reviews/_ajax?ref_=undefined&paginationKey=g4xolermtiqhejcxxxgs753i36t52q343mmtv4ppate6qp3ulnswic7qi46ra7j6mgfuqoxn";
//        List<Review_DTO> reviews = new ArrayList<Review_DTO>();
//
//        try {
//            Document doc=Jsoup.connect(reviews_url)
//                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
//                    .header("Accept-Language", "en")
//                    .header("Accept-Encoding","gzip,deflate,sdch")
//                    .get();
//
//            Elements listE = doc.select("div[class*=\"imdb-user-review\"]");
//            for(Element e : listE){
//                Review_DTO review = new Review_DTO();
//
//                Element tmp = e.getElementsByClass("display-name-date").first();
//                String username = tmp.getElementsByClass("display-name-link").text();
//                review.setUsername(username);
//
//                String date = tmp.getElementsByClass("review-date").text();
//                review.setDate(date);
//
//                Element tmp1 = e.getElementsByClass("rating-other-user-rating").first();
//                String rate="This user doesn't rate the movie";
//                if(tmp1!=null) rate = tmp1.getElementsByTag("span").get(0).text();
//                review.setRate(rate);
//
//                String title = e.getElementsByClass("title").text();
//                review.setTitle(title);
//
//                Element tmp2 = e.getElementsByClass("text show-more__control").first();
//                String content="";
//                if(tmp2!=null) content=tmp2.text();
//                review.setContent(content);
//
//                reviews.add(review);
//            }
//            return reviews;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return reviews;
//
//    }
//
//    public static String getTrailerUrl(String movieID){
//        String movie_url = "https://www.imdb.com/title/"+movieID;
//        String trailer_url = "1";
//        try {
//            Connection.Response res = Jsoup.connect(movie_url)
//                    .method(Connection.Method.GET)
//                    .execute();
//            System.out.println(res.body());
//            Document doc = Jsoup.connect(movie_url)
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")
//                    .header("Accept-Language", "en")
//                    .header("Accept-Encoding", "gzip,deflate,sdch")
//                    .get();
//            String trailer_page_href = doc.getElementsByClass("Media__SlateContainer-sc-1x98dcb-4 dDhYrh").first()
//                    .select("a").attr("href");
//            System.out.println("trailer" + trailer_page_href);
//
//            String trailer_page_url = "https://www.imdb.com"+trailer_page_href;
//            doc = Jsoup.connect(trailer_page_url)
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")
//                    .header("Accept-Language", "en")
//                    .header("Accept-Encoding", "gzip,deflate,sdch")
//                    .get();
//            String trailer_scrip = doc.getElementsByTag("script").get(18).toString(); //script 18 có url video
//            trailer_url = StringUtils.substringBetween(trailer_scrip,
//                    "{\\\"definition\\\":\\\"480p\\\",\\\"mimeType\\\":\\\"video/mp4\\\",\\\"url\\\":\\\"",
//                    "\\\"},{\\\"definition\\\":\\\"SD\\\",\\\"mimeType\\\":\\\"video/mp4\\\",\\\"url\\\":\\"); //cut từ scrip để có url video
//
//            return trailer_url;
//
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        return trailer_url;
//    }
//
//    public static Movie_DTO getMovie(String MovieID){
//        Movie_DTO movie = new Movie_DTO();
//        String API_key = "k_95jrg3qr";
//        String movie_url="https://imdb-api.com/en/API/Title/"+API_key+"/"+MovieID;
//        System.out.println(movie_url);
//        try {
//            Connection.Response res = Jsoup.connect(movie_url)
//                    .ignoreContentType(true)
//                    .method(Connection.Method.GET)
//                    .execute();
//
//            JSONParser parser = new JSONParser();
//            JSONObject json = (JSONObject) parser.parse(res.body());
//            String title = (String) json.get("fullTitle");
//            String image = (String) json.get("image");
//            String trailer = getTrailerUrl(MovieID);
//            String rating = (String) json.get("imDbRating");
//            String rating_vote = (String) json.get("imDbRatingVotes");
//            String tagline = (String) json.get("tagline");
//            String plot = (String) json.get("plot");
//            String releaseDate = (String) json.get("releaseDate");
//            String runtimeMins = (String) json.get("runtimeMins");
//            String directors = (String) json.get("directors");
//            String writers = (String) json.get("writers");
//            List<Actor_DTO> actors = new ArrayList<Actor_DTO>();
//            JSONArray jsonArray = (JSONArray) json.get("actorList");
//            for(Object obj : jsonArray){
//                JSONObject jsonObj = (JSONObject) obj;
//                Actor_DTO actor = new Actor_DTO();
//                actor.setId((String)jsonObj.get("id"));
//                actor.setName((String)jsonObj.get("name"));
//                actor.setCharacter((String)jsonObj.get("asCharacter"));
//                actor.setImage((String)jsonObj.get("image"));
//                actors.add(actor);
//            }
//            String type = (String) json.get("type");
//            String genres = (String) json.get("genres");
//            String companies = (String) json.get("companies");
//            String countries = (String) json.get("countries");
//            String languages = (String) json.get("languages");
//            String contentRating = (String) json.get("contentRating");
//            String keywords = (String) json.get("keywords");
//            List<Review_DTO> reviews = new ArrayList<Review_DTO>();
//            reviews = getReviews(MovieID);
//            movie = new Movie_DTO(MovieID, title, image,  rating, rating_vote, tagline,
//                    plot, releaseDate, runtimeMins, directors, writers, actors, type, genres, companies,
//                    countries, languages, contentRating, keywords, reviews);
//        } catch (IOException | ParseException e){
//            e.printStackTrace();
//        }
//        return movie;
//    }
//
//    public static void main(String[] args) {
//        List<MovieSearchResult_DTO> moviesSearchResults = new ArrayList<MovieSearchResult_DTO>();
//        moviesSearchResults = listMoviebyKeyword("avengers");
//        for(int i=0; i<moviesSearchResults.size(); i++) {
//            MovieSearchResult_DTO movieSearchResult = new MovieSearchResult_DTO();
//            movieSearchResult = moviesSearchResults.get(i);
//            System.out.println(movieSearchResult.getTitle());
//            System.out.println(movieSearchResult.getImg());
//            System.out.println(movieSearchResult.getRating());
//            System.out.println(movieSearchResult.getDirector());
//            System.out.println(movieSearchResult.getStars());
//            System.out.println("\n");
//        }
//
////        List<Review_DTO> reviews = new ArrayList<Review_DTO>();
////        reviews=getReviews("tt0145487");
////        for(int i=0; i<reviews.size(); i++){
////            Review_DTO review = new Review_DTO();
////            review = reviews.get(i);
////            System.out.println(review.getUsername());
////            System.out.println(review.getDate());
////            System.out.println(review.getRate());
////            System.out.println(review.getTitle());
////            System.out.println(review.getContent());
////            System.out.println("\n");
////        }
////
////        Movie_DTO movie = new Movie_DTO();
////        movie = getMovie("tt2674426");
////        System.out.println("Title: "+movie.getTitle());
////        System.out.println("IMDB Rating: "+movie.getImDbRating());
////        System.out.println("Trailer: "+movie.getTrailer());
////        System.out.println("Genres: "+movie.getGenres());
////        System.out.println("Directors: "+movie.getDirectors());
////        System.out.println("<<<  ActorList  >>>");
////        for(Actor_DTO actor: movie.getActors()){
////            System.out.println(actor.getName() +" as "+actor.getCharacter());
////        }
////        System.out.println("<<<  Reviews  >>>");
////        for(Review_DTO review: movie.getReviews()){
////            System.out.println("User: ["+review.getUsername()+"] --- Date: "+ review.getDate());
////            System.out.println("\""+review.getTitle()+"\"");
////        }
//    }
//}
