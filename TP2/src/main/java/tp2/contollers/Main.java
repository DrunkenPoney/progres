package tp2.contollers;

import tp2.models.db.collections.LocalClientCollection;
import tp2.models.db.documents.ClientModel;
import tp2.models.db.documents.GroupModel;
import tp2.models.io.Message;
import tp2.models.io.Transmission;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static tp2.models.db.collections.Accessors.*;
import static tp2.models.utils.SGR.*;

public class Main {
	public static List<ClientModel> exec(String name) throws Exception {
		System.out.println(FG_BRIGHT_BLUE.wrap("SERVER ADDRESS: ") +
		                   FG_BRIGHT_YELLOW.wrap(((InetSocketAddress)
				                   Transmission.getInstance().getLocalAddress()).getAddress().getHostAddress()));
		
		initLocalClient(name, (InetSocketAddress) Transmission.getInstance().getLocalAddress());
		
		
		System.out.println("Handler added? " + Transmission.getInstance().addMessageHandler(message -> System.out.println(
				format("%s sent: %s",
				       FG_BRIGHT_YELLOW.wrap(message.getSender().getName()),
				       FG_BRIGHT_GREEN.wrap(message.getMessage())))));
		
		List<ClientModel> clients = getClientsCollection().getOnlineClients();
		
		System.out.println("Online clients:\n\t> " +
		                   clients.stream()
		                          .map(client -> format("%s (%s:%s)",
		                                                client.getName(),
		                                                client.getAddress(),
		                                                client.getPort()))
		                          .collect(Collectors.joining("\n\t> ")));
		return clients;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static class MainBob {
		public static void main(String[] args) throws Exception {
			exec("Bob");
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static class MainCharlie {
		public static void main(String[] args) throws Exception {
			List<ClientModel> clients     = exec("Charlie");
			ClientModel       localClient = ((LocalClientCollection) getClientsCollection()).getLocalClient();
			clients.remove(localClient);
			
			GroupModel groupModel = new GroupModel("Bob & Charlie", localClient, clients.toArray(new ClientModel[0]));
			getGroupsCollection().save(groupModel);
			
			Transmission.getInstance().send(new Message(localClient, "Hey! Do you copy?", groupModel));
			
		}
	}
}
