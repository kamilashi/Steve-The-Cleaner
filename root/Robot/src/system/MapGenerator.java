package system;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.BasicConfigurator;

import com.google.gson.Gson;

public class MapGenerator {
	ActiveMQConnectionFactory jmsConnectionFactory;

    public MapGenerator()
    {
        Map map = Map.getInstance();

        map.addRoom(0, 140, (140+140), 70, -100, -100, -100, -100);
        map.addRoom(0, 0, 140, 140, 70, 140, 110, 140);
        map.addRoom(140, 0, 140, 140, (140+30), 140, (140+70), 140);
        
        scan(map);
    }

   public void scan (Map map)
   {
	   int count = 0;

       for(Map.Room room : map.rooms)
       {
           int i,j;
           for ( i = room.x+1; i<(room.x+room.width); i++)
           {
               for ( j = room.y+1; j<(room.y+room.length); j++)
               {
                   if(count == 1000){
                       count = 0;
                   try {Thread.sleep(1000);                            //simulate the time delay while scanning every 100 tuples;
                   		sendUpdate(map);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }}   
                   count++;
                   Map.Tuple tuple = map.new Tuple(i,j);
                   map.coordinates.put(tuple.toString(),tuple);
                   System.out.println(tuple.toString());
               }
           }

           Map.Tuple tuple = map.new Tuple(room.doorX1,room.doorY1 );

           if(tuple.X >= 0)			//if there is a valid door
           {
               float cumulative = (float) 0.01;
               while ((tuple.X < room.doorX2)||(tuple.Y < room.doorY2))
               {
                   count++;
                   tuple = map.lerp(room.doorX1,room.doorY1,room.doorX2,room.doorY2,cumulative);
                   //System.out.println("lerp "+ tuple.toString() + "   at " + cumulative + " %");
                   map.coordinates.put(tuple.toString(),tuple);
                   cumulative+=0.005;

               }
           }
       }
       
       map.generated = true;
       System.out.println("Map has been generated!");
       sendUpdate(map);
	   
	   
   }
    
   public void sendUpdate(Map map)
   {
	   String jsonObject = new Gson().toJson(map);
	   
	   try {
		   BasicConfigurator.configure();
		   jmsConnectionFactory = new ActiveMQConnectionFactory("tcp://" + "localhost" + ":" + 61616);
		   
		Connection connection = jmsConnectionFactory.createConnection("admin","admin");
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue dataQueue = session.createQueue("Map Data");
		MessageProducer producer = session.createProducer(dataQueue);
		TextMessage textMessage = session.createTextMessage(jsonObject);
		producer.send(textMessage);
		
		System.out.println("Sent message to queque!");
		   
	} catch (JMSException e) {
		e.printStackTrace();
		System.out.println("Failed to send data to the queue.");
	}
   }

}
