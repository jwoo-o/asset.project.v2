package com.gen.vacation.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by jinwoo.
 * User: jwoh
 * Date: 2020-04-14
 * Time: 오후 4:15
 */
public class JsonUtil {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);


    public static JSONObject dtoToJson(Object obj) throws JsonProcessingException, ParseException {


        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(obj);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(json);

        return jsonObj;

    }

    public static String dtoToString(Object obj) throws JsonProcessingException {


        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(obj);

        return json;

    }


    public static Object stringToJson(String str) {

        JSONParser jsonParser = new JSONParser();
        Object obj;
        try {
            obj = jsonParser.parse(str);
        } catch (Exception e) {
            return null;
        }
        return obj;
    }

    public static Object stringToClass(String str, Class obj) {

        JSONParser jsonParser = new JSONParser();
        ModelMapper modelMapper = new ModelMapper();
        try {
            JSONObject jsonObj = (JSONObject) jsonParser.parse(str);
            return modelMapper.map(jsonObj, obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Map<String, Object>> convertorTreeMap(List inList, String rootId, final String idKey, final String pIdKey) {
        List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();  // 최종 트리

        if (inList == null || inList.size() == 0) throw new IllegalArgumentException("List<Map> 데이터가 없습니다.");
        if (inList.get(0) == null) throw new IllegalArgumentException("Map 데이터가 없습니다.");


        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();// 원본데이터(Bean일경우 Map으로 변환)
        Iterator iter;
        for (iter = inList.iterator(); iter.hasNext(); ) {
            try {
                Object obj = iter.next();
                if (obj instanceof Map) {
                    list.add((Map<String, Object>) obj);
                } else {
                    list.add(PropertyUtils.describe(obj));
                }
            } catch (Exception e) {
                throw new RuntimeException("Collection -> List<Map> 으로 변환 중 실패: " + e);
            }
        }


        int listLength = list.size();
        int loopLength = 0;
        final int[] treeLength = new int[]{0};

        while (treeLength[0] != listLength && listLength != loopLength++) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> item = list.get(i);
                if (rootId.equals(item.get(pIdKey))) {
                    Map<String, Object> view = new HashMap<String, Object>(item);
                    view.put("id", item.get("orgCode"));
                    view.put("label", item.get("orgName"));
                    view.put("value", item.get("orgCode"));
                    view.put("children", new ArrayList<Map<String, Object>>());

                    treeList.add(view);
                    list.remove(i);

                    treeLength[0]++;
                    break;

                } else {
                    new InnerClass() {
                        public void getParentNode(List<Map<String, Object>> children, Map<String, Object> item) {
                            for (int i = 0; i < children.size(); i++) {
                                Map<String, Object> child = children.get(i);
                                if (child.get(idKey).equals(item.get(pIdKey))) {
                                    Map<String, Object> view = new HashMap<String, Object>(item);
                                    view.put("id", item.get("orgCode"));
                                    view.put("label", item.get("orgName"));
                                    view.put("children", new ArrayList<Map<String, Object>>());
                                    ((List<Map<String, Object>>) child.get("children")).add(view);
                                    treeLength[0]++;
                                    list.remove(list.indexOf(item));

                                    break;
                                } else {
                                    if (((List<Map<String, Object>>) child.get("children")).size() > 0) {
                                        getParentNode((List<Map<String, Object>>) child.get("children"), item);
                                    }
                                }
                            }
                        }
                    }.getParentNode(treeList, item);
                }
            }
        }
        return treeList;
    }

    public static List<Map<String, Object>> convertorTreeMap2(List inList, String rootId, final String idKey, final String pIdKey) {
        List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();  // 최종 트리

        if (inList == null || inList.size() == 0) throw new IllegalArgumentException("List<Map> 데이터가 없습니다.");
        if (inList.get(0) == null) throw new IllegalArgumentException("Map 데이터가 없습니다.");


        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();// 원본데이터(Bean일경우 Map으로 변환)
        Iterator iter;
        for (iter = inList.iterator(); iter.hasNext(); ) {
            try {
                Object obj = iter.next();
                if (obj instanceof Map) {
                    list.add((Map<String, Object>) obj);
                } else {
                    list.add(PropertyUtils.describe(obj));
                }
            } catch (Exception e) {
                throw new RuntimeException("Collection -> List<Map> 으로 변환 중 실패: " + e);
            }
        }


        int listLength = list.size();
        int loopLength = 0;
        final int[] treeLength = new int[]{0};

        while (treeLength[0] != listLength && listLength != loopLength++) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> item = list.get(i);
                if (rootId.equals(item.get(pIdKey))) {
                    Map<String, Object> view = new HashMap<String, Object>(item);
                    view.put("id", item.get("orgCode"));
                    view.put("label", item.get("orgName"));
                    view.put("children", new ArrayList<Map<String, Object>>());

                    treeList.add(view);
                    list.remove(i);

                    treeLength[0]++;
                    break;

                } else {
                    new InnerClass() {
                        public void getParentNode(List<Map<String, Object>> children, Map<String, Object> item) {
                            for (int i = 0; i < children.size(); i++) {
                                Map<String, Object> child = children.get(i);
                                if (child.get(idKey).equals(item.get(pIdKey))) {
                                    Map<String, Object> view = new HashMap<String, Object>(item);
                                    view.put("value", item.get("orgCode"));
                                    view.put("label", item.get("orgName"));
                                    view.put("children", new ArrayList<Map<String, Object>>());
                                    ((List<Map<String, Object>>) child.get("children")).add(view);
                                    treeLength[0]++;
                                    list.remove(list.indexOf(item));

                                    break;
                                } else {
                                    if (((List<Map<String, Object>>) child.get("children")).size() > 0) {
                                        getParentNode((List<Map<String, Object>>) child.get("children"), item);
                                    }
                                }
                            }
                        }
                    }.getParentNode(treeList, item);
                }
            }
        }
        return treeList;
    }

    public interface InnerClass {
        public void getParentNode(List<Map<String, Object>> list, Map<String, Object> item);
    }

}
