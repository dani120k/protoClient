package protoClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;


public class DemoClient {
  
  static final String HOST = System.getProperty("host", "127.0.0.1");
  static final int PORT = Integer.parseInt(System.getProperty("port", "8463"));

  public static void checkGetHotelListRequest(Bootstrap bootstrap) throws Exception{
    // Create connection
    Channel c = bootstrap.connect(HOST, PORT).sync().channel();

    // Get handle to handler so we can send message
    DemoClientHandler handle = c.pipeline().get(DemoClientHandler.class);
    System.out.println("Send get hotel list request: date 2018-11-11, city city_name, max_price 15000");
    protoClient.DemoProtocol.DemoResponse resp = handle.sendGetHotelListRequest("2018-11-11", "city_name", 15000);
    c.close();

    protoClient.DemoProtocol.DemoResponse.ResponseGetHotelList dataMessage = resp.getHotelRequest();
    List<protoClient.DemoProtocol.DemoResponse.Hotel> hotelList = dataMessage.getHotelsList();
    System.out.println("Response:");
    for (protoClient.DemoProtocol.DemoResponse.Hotel hotel: hotelList
    ) {
      System.out.println(hotel.getHotelId() + " " + hotel.getName() + " " + hotel.getMinPrice() + " " + hotel.getMaxPrice());
    }
  }

  public static void checkGetRoomsRequest(Bootstrap bootstrap) throws Exception{
    // Create connection
    Channel c = bootstrap.connect(HOST, PORT).sync().channel();

    // Get handle to handler so we can send message
    DemoClientHandler handle = c.pipeline().get(DemoClientHandler.class);
    System.out.println("Send get rooms request: date 2018-11-11, hotel_id 1");
    protoClient.DemoProtocol.DemoResponse resp = handle.sendGetRoomsListRequest(1, "2018-11-11");
    c.close();

    protoClient.DemoProtocol.DemoResponse.ResponseGetRoomList dataMessage = resp.getRoomsRequest();
    List<protoClient.DemoProtocol.DemoResponse.Room> roomsList = dataMessage.getRoomsList();
    System.out.println("Response:");
    for (protoClient.DemoProtocol.DemoResponse.Room room: roomsList
    ) {
      System.out.println(room.getPrice() + " " + room.getType());
    }
  }

  public static void checkBookHotelRequest(Bootstrap bootstrap) throws Exception{
    // Create connection
    Channel c = bootstrap.connect(HOST, PORT).sync().channel();

    // Get handle to handler so we can send message
    DemoClientHandler handle = c.pipeline().get(DemoClientHandler.class);
    System.out.println("Send book hotel request: hotel_id 1, date 2018-11-11, cvv 666, card_number 1234, fname FirstName, sName SurName");
    protoClient.DemoProtocol.DemoResponse resp = handle.sendBookHotelRequest(1, "2018-11-11", 666, "1234","FirstName", "SurName");
    c.close();

    protoClient.DemoProtocol.DemoResponse.ResponseBookHotel dataMessage = resp.getBookRequest();
    Boolean result = dataMessage.getResult();
    System.out.println("Response:");
    System.out.println("Result is " + result);
  }



  public static void main(String[] args) throws InterruptedException {
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(group)
               .channel(NioSocketChannel.class)
               .handler(new DemoClientInitializer());
      
      try {
        checkGetHotelListRequest(bootstrap);

        checkGetRoomsRequest(bootstrap);

        checkBookHotelRequest(bootstrap);


      }catch (Exception ex){
        ex.printStackTrace();
      }

    } finally {
      group.shutdownGracefully();
    }
    
  }
}
