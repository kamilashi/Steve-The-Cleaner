package program;

import java.io.IOException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.BasicConfigurator;

import com.google.gson.Gson;

public class MapReceiver implements Runnable{
	
	public MapReceiver() throws IOException
	
	{
		Map map =  Map.getInstance();								
															//for the sake of this program compiling. Shouldn't be instantiated here!!!
		
	    if(map != null)
	    {
	    map.generated = false;
	    //map.addRoom(0, 140, (140+140), 70, -100, -100, -100, -100);
		//map.addRoom(0, 0, 140, 140, 70, 140, 110, 140);
		//map.addRoom(140, 0, 140, 140, (140+30), 140, (140+70), 140);
		MapManager.storeMap(map);
	    }
	}

	@Override
	public void run() {
		
		try {
			BasicConfigurator.configure();
			ActiveMQConnectionFactory jmsConnectionFactory = new ActiveMQConnectionFactory("tcp://" + "localhost" + ":" + 61616);
			Connection connection;
			connection = jmsConnectionFactory.createConnection("admin","admin");
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue dataQueue = session.createQueue("Map Data");
			MessageConsumer consumer = session.createConsumer(dataQueue);
			
			//infinite loop that listens for messages
			while(true)
			{
				TextMessage textMessage = (TextMessage) consumer.receive();
				String payload = textMessage.getText();				
				
				Map newMap = new Gson().fromJson(payload, Map.class);
				MapManager.storeMap(newMap);
				//MapManager.getMap().setMapData(mapData);
				if(newMap.generated)										//relocate to setGenerated lated
				{connection.close();
				break;
				}
			}
			
			
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("Failed to fetch data from the queue.");
		} catch (IOException e) {
			System.out.println("Failed to assing a new map.");
			e.printStackTrace();
		}
	}
	
	

}
