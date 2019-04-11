import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.enums.FriendsOrder;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.queries.friends.FriendsGetQuery;

import java.util.Iterator;

public class VkClient {

    private JsonParser parser = new JsonParser();
    private TransportClient transportClient = HttpTransportClient.getInstance();
    private VkApiClient vkApiClient = new VkApiClient(transportClient);

    private OpenedFriend myAccount;

    public void run(){
        createMainFriend();
    }

    private void createMainFriend(){

        UserActor userActor = new UserActor(65309196, "0c47dedbe00ce5d04416d0e379a0b039fd9ef8672c5349242ae24a1af01a4b7b31ecbea39c4e8e558ac7f");
        ServiceActor serviceActor = new ServiceActor(6935612, "z3Q3XRmtHYLPGjWFJI03","aa0ecda2aa0ecda2aa0ecda2fdaa67199eaaa0eaa0ecda2f6a54bbdb9d93017b2418f07" );

        FriendsGetQuery friendsGetQuery ;
        friendsGetQuery = vkApiClient.friends().get(serviceActor).userId(userActor.getId())
                //.order(FriendsOrder.HINTS)
                .count(50)
                .fields(Fields.ABOUT);

        String resp = null;
        try {
            resp = friendsGetQuery.executeAsString();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = parser.parse(resp).getAsJsonObject();
        JsonObject response = jsonObject.getAsJsonObject("response");
        JsonArray friendsArray = (JsonArray) response.get("items");
        Iterator friendsItr = friendsArray.iterator();

        myAccount = new OpenedFriend(userActor.getId(),"Я");

        while (friendsItr.hasNext()) {
            JsonObject test = (JsonObject) friendsItr.next();
            String ID = test.get("id").toString();
            String name =test.get("first_name").toString()+" "+test.get("last_name".toString());
            name = name.replaceAll("\"","");
            JsonElement isClosed = test.get("is_closed");
            String isClosedString;
            if (isClosed != null) {
                isClosedString = isClosed.toString();
            }
            else
            {
                isClosedString = "true";
            }

            if (isClosedString.equals("false")){
                myAccount.add(new OpenedFriend(Integer.parseInt(ID),name));
            }
            else {
                myAccount.add(new ClosedFriend(Integer.parseInt(ID),name));
            }

        }
        for (Friend friend: myAccount.getFriends() ) {
            fullFriend(friend);
        }

    }

    private void fullFriend(Friend friend){

        ServiceActor serviceActor = new ServiceActor(6935612, "z3Q3XRmtHYLPGjWFJI03","aa0ecda2aa0ecda2aa0ecda2fdaa67199eaaa0eaa0ecda2f6a54bbdb9d93017b2418f07" );
        if (friend instanceof OpenedFriend) {

            FriendsGetQuery friendsGetQuery;
            friendsGetQuery = vkApiClient.friends().get(serviceActor).userId(friend.getID())
                    .order(FriendsOrder.HINTS)
                    .fields(Fields.ABOUT);

            String resp = null;
            try {
                resp = friendsGetQuery.executeAsString();
            } catch (ClientException e) {
                e.printStackTrace();
            }

            JsonObject jsonObject = parser.parse(resp).getAsJsonObject();
            JsonObject response = jsonObject.getAsJsonObject("response");
            JsonArray friendsArray = (JsonArray) response.get("items");
            Iterator friendsItr = friendsArray.iterator();


            while (friendsItr.hasNext()) {
                JsonObject test = (JsonObject) friendsItr.next();
                String IDString = test.get("id").toString();
                String name =test.get("first_name").toString()+" "+test.get("last_name").toString();
                name = name.replaceAll("\"","");
                JsonElement isClosed = test.get("is_closed");
                String isClosedString;
                if (isClosed != null) {
                    isClosedString = isClosed.toString();
                } else {
                    isClosedString = "true";
                }

                if (isClosedString.equals( "false")) {
                    ((OpenedFriend) friend).add(new OpenedFriend(Integer.parseInt(IDString),name));
                } else {
                    ((OpenedFriend) friend).add(new ClosedFriend(Integer.parseInt(IDString),name));
                }

            }
        }

    }

    public OpenedFriend getMyAccount(){
        return myAccount;
    }
}
