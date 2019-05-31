package protoClient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import protoClient.DemoProtocol.AnyRequest.*;
import protoClient.DemoProtocol.DemoResponse;
import protoClient.DemoProtocol.AnyRequest;


public class DemoClientHandler extends SimpleChannelInboundHandler<protoClient.DemoProtocol.DemoResponse> {

    private Channel channel;
    private protoClient.DemoProtocol.DemoResponse resp;
    BlockingQueue<protoClient.DemoProtocol.DemoResponse> resps = new LinkedBlockingQueue<protoClient.DemoProtocol.DemoResponse>();


    public DemoResponse sendGetHotelListRequest(String date, String city, int max_price) {


      GetHotelList hotelList = GetHotelList.newBuilder().setCity(city).setDate(date).setMaxPrice(max_price).build();

    AnyRequest req = AnyRequest.newBuilder()
                            .setHotelRequest(hotelList).build();
    
    // Send request
    channel.writeAndFlush(req);
    
    // Now wait for response from server
    boolean interrupted = false;
    for (;;) {
        try {
            resp = resps.take();
            break;
        } catch (InterruptedException ignore) {
            interrupted = true;
        }
    }

    if (interrupted) {
        Thread.currentThread().interrupt();
    }
    
    return resp;
  }

    public DemoResponse sendGetRoomsListRequest(int hotel_id, String date) {


        GetRoomList roomList = GetRoomList.newBuilder().setHotelId(hotel_id).setDate(date).build();

        AnyRequest req = AnyRequest.newBuilder()
                .setRoomsRequest(roomList).build();

        // Send request
        channel.writeAndFlush(req);

        // Now wait for response from server
        boolean interrupted = false;
        for (;;) {
            try {
                resp = resps.take();
                break;
            } catch (InterruptedException ignore) {
                interrupted = true;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }

        return resp;
    }

    public DemoResponse sendBookHotelRequest(int hotel_id, String date, int cvv, String card_number, String fName, String sName) {


        BookHotel bookHotel = BookHotel.newBuilder().setHotelId(hotel_id).setCVV(cvv).setCardNumber(card_number).setDate(date).setFirstName(fName).setSurname(sName).build();
        AnyRequest req = AnyRequest.newBuilder()
                .setBookRequest(bookHotel).build();

        // Send request
        channel.writeAndFlush(req);

        // Now wait for response from server
        boolean interrupted = false;
        for (;;) {
            try {
                resp = resps.take();
                break;
            } catch (InterruptedException ignore) {
                interrupted = true;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }

        return resp;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
      channel = ctx.channel();
  }
  
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, protoClient.DemoProtocol.DemoResponse msg)
        throws Exception {
        resps.add(msg);
    }
  
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
