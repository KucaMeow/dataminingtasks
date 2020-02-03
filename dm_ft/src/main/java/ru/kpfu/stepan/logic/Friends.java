package ru.kpfu.stepan.logic;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.Fields;
import com.vk.api.sdk.objects.groups.GroupFull;
import ru.kpfu.stepan.App;

import java.util.*;
import java.util.stream.Collectors;

public class Friends {

    public static List<Integer> getCloseIDs(UserActor user) throws ClientException, ApiException, InterruptedException {
        List<Integer> friends_ids = new LinkedList<>();

        System.out.println("It will return friends who different less than coefficient");
        System.out.println("Enter coefficient (in percents 0 - 100):");
        int per = App.SC.nextInt();

        List<Integer> user_friendid_list = App.vk.friends().get(user).execute().getItems();
        List<String> user_groupid_list = App.vk.groups().get(user).execute().getItems().stream().map(Objects::toString).collect(Collectors.toList());
        List<GroupFull> user_group_list = App.vk.groups().getById(user).groupIds(user_groupid_list).fields(Fields.ACTIVITY).execute();
        int user_g_count = user_group_list.size(); //Count of user's group
        Map<String, Integer> user_groups_activities = new HashMap<>();
        for (GroupFull gf : user_group_list) {
            if(!user_groups_activities.containsKey(gf.getActivity()))
                user_groups_activities.put(gf.getActivity(), 1);
            else
                user_groups_activities.put(gf.getActivity(),
                        user_groups_activities.get(gf.getActivity()) + 1);
        }


        Map<String, Integer> friend_groups_activities;
        List<String> friend_groupid_list;
        List<GroupFull> friend_group_list;
        int difference;
        double percent_difference;
        int i = 0;
            for(int friend_id : user_friendid_list) {
                percent_difference = 0;
                difference = 0;
                friend_groups_activities = new HashMap<>();

                Thread.sleep(700);
                    friend_groupid_list = App.vk.groups().get(user).userId(friend_id).execute().getItems().stream().map(Objects::toString).collect(Collectors.toList());
                    if(friend_groupid_list.isEmpty()) continue;
                    friend_group_list = App.vk.groups().getById(user).groupIds(friend_groupid_list).fields(Fields.ACTIVITY).execute();


                for (GroupFull gf : friend_group_list) {
                    if(!friend_groups_activities.containsKey(gf.getActivity()))
                        friend_groups_activities.put(gf.getActivity(), 1);
                    else
                        friend_groups_activities.put(gf.getActivity(),
                                friend_groups_activities.get(gf.getActivity()) + 1);
                }

                for(Map.Entry<String, Integer> act_entry : user_groups_activities.entrySet()) {
                    String activity = act_entry.getKey();
                    int u_c = user_groups_activities.get(activity);
                    int f_c = friend_groups_activities.get(activity) == null ? 0 : friend_groups_activities.get(activity);
                    difference = Math.abs(u_c - f_c);
                    percent_difference += 100.0*difference/Math.max(u_c, f_c);
                }

                percent_difference = percent_difference/user_groups_activities.entrySet().size();
                if(percent_difference < per) friends_ids.add(friend_id);
                System.out.println(++i + "/" + user_friendid_list.size());
            }

        return friends_ids;
    }

    public static List<Integer> getSameIDs(UserActor user) throws ClientException, ApiException, InterruptedException {
        List<Integer> friends_ids = new LinkedList<>();

        System.out.println("It will return friends who's groups with same activities differ less than coefficient");
        System.out.println("Enter coefficient");
        int per = App.SC.nextInt();

        List<Integer> user_friendid_list = App.vk.friends().get(user).execute().getItems();
        List<String> user_groupid_list = App.vk.groups().get(user).execute().getItems().stream().map(Objects::toString).collect(Collectors.toList());
        List<GroupFull> user_group_list = App.vk.groups().getById(user).groupIds(user_groupid_list).fields(Fields.ACTIVITY).execute();
        Map<String, Integer> user_groups_activities = new HashMap<>();
        for (GroupFull gf : user_group_list) {
            if(!user_groups_activities.containsKey(gf.getActivity()))
                user_groups_activities.put(gf.getActivity(), 1);
            else
                user_groups_activities.put(gf.getActivity(),
                        user_groups_activities.get(gf.getActivity()) + 1);
        }


        Map<String, Integer> friend_groups_activities;
        List<String> friend_groupid_list;
        List<GroupFull> friend_group_list;
        int i = 0;
        for(int friend_id : user_friendid_list) {
            friend_groups_activities = new HashMap<>();

            Thread.sleep(700);
            friend_groupid_list = App.vk.groups().get(user).userId(friend_id).execute().getItems().stream().map(Objects::toString).collect(Collectors.toList());
            if(friend_groupid_list.isEmpty()) continue;
            friend_group_list = App.vk.groups().getById(user).groupIds(friend_groupid_list).fields(Fields.ACTIVITY).execute();


            for (GroupFull gf : friend_group_list) {
                if(!friend_groups_activities.containsKey(gf.getActivity()))
                    friend_groups_activities.put(gf.getActivity(), 1);
                else
                    friend_groups_activities.put(gf.getActivity(),
                            friend_groups_activities.get(gf.getActivity()) + 1);
            }

            boolean flag = false;
            for(Map.Entry<String, Integer> act_entry : user_groups_activities.entrySet()) {
                String activity = act_entry.getKey();
                int u_c = user_groups_activities.get(activity);
                int f_c = friend_groups_activities.get(activity) == null ? 0 : friend_groups_activities.get(activity);
                if(Math.abs(u_c - f_c) > per) {
                    flag = true;
                    break;
                }
            }
            System.out.println(++i + "/" + user_friendid_list.size());
            if(flag) continue;
            friends_ids.add(friend_id);
        }

        return friends_ids;
    }

    public static List<Integer> getFarIDs(UserActor user) throws ClientException, ApiException, InterruptedException {
        List<Integer> friends_ids = new LinkedList<>();

        List<Integer> user_friendid_list = App.vk.friends().get(user).execute().getItems();
        List<String> user_groupid_list = App.vk.groups().get(user).execute().getItems().stream().map(Objects::toString).collect(Collectors.toList());
        List<GroupFull> user_group_list = App.vk.groups().getById(user).groupIds(user_groupid_list).fields(Fields.ACTIVITY).execute();
        Map<String, Integer> user_groups_activities = new HashMap<>();
        for (GroupFull gf : user_group_list) {
            if(!user_groups_activities.containsKey(gf.getActivity()))
                user_groups_activities.put(gf.getActivity(), 1);
            else
                user_groups_activities.put(gf.getActivity(),
                        user_groups_activities.get(gf.getActivity()) + 1);
        }


        Map<String, Integer> friend_groups_activities;
        List<String> friend_groupid_list;
        List<GroupFull> friend_group_list;
        int i = 0;
        for(int friend_id : user_friendid_list) {
            friend_groups_activities = new HashMap<>();

            Thread.sleep(700);
            friend_groupid_list = App.vk.groups().get(user).userId(friend_id).execute().getItems().stream().map(Objects::toString).collect(Collectors.toList());
            if(friend_groupid_list.isEmpty()) continue;
            friend_group_list = App.vk.groups().getById(user).groupIds(friend_groupid_list).fields(Fields.ACTIVITY).execute();


            for (GroupFull gf : friend_group_list) {
                if(!friend_groups_activities.containsKey(gf.getActivity()))
                    friend_groups_activities.put(gf.getActivity(), 1);
                else
                    friend_groups_activities.put(gf.getActivity(),
                            friend_groups_activities.get(gf.getActivity()) + 1);
            }

            boolean flag = false;
            for(Map.Entry<String, Integer> act_entry : user_groups_activities.entrySet()) {
                String activity = act_entry.getKey();
                if(friend_groups_activities.get(activity) == null) {
                    flag = true;
                    break;
                }
            }
            System.out.println(++i + "/" + user_friendid_list.size());
            if(flag) continue;
            friends_ids.add(friend_id);
        }

        return friends_ids;
    }
}
