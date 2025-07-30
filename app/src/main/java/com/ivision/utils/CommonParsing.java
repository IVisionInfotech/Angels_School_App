package com.ivision.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ivision.model.Attendance;
import com.ivision.model.CommonModel;
import com.ivision.model.Gallery;
import com.ivision.model.Management;
import com.ivision.model.MonthData;
import com.ivision.model.News;
import com.ivision.model.Notification;
import com.ivision.model.Product;
import com.ivision.model.Result;
import com.ivision.model.Review;
import com.ivision.model.Subject;
import com.ivision.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommonParsing {

    private static String TAG = "CommonParsing";

    public static User bindUserData(JsonObject object, User model) {

        if (object.has("id")) model.setId(Integer.parseInt(object.get("id").getAsString()));
        if (object.has("rollno"))
            model.setRollNo(Integer.parseInt(object.get("rollno").getAsString()));
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("mobile")) model.setContact(object.get("mobile").getAsString());
        if (object.has("email")) model.setEmailId(object.get("email").getAsString());
        if (object.has("image")) model.setImage(object.get("image").getAsString());

        return model;
    }

    public static CommonModel bindCommonData(JsonObject object) {

        CommonModel model = new CommonModel();
        if (object.has("id"))
            model.setId(object.get("id").isJsonNull() ? "" : object.get("id").getAsString());
        if (object.has("title"))
            model.setTitle(object.get("title").isJsonNull() ? "" : object.get("title").getAsString());
        if (object.has("topic"))
            model.setTitle(object.get("topic").isJsonNull() ? "" : object.get("topic").getAsString());
        if (object.has("file"))
            model.setFile(object.get("file").isJsonNull() ? "" : object.get("file").getAsString());
        if (object.has("stateId")) model.setStateId(object.get("stateId").getAsString());
        if (object.has("code")) model.setCode(object.get("code").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("image")) model.setImage(object.get("image").getAsString());
        if (object.has("description"))
            model.setDescription(object.get("description").getAsString());
        if (object.has("dateTimeAdded"))
            model.setDateTimeAdded(object.get("dateTimeAdded").getAsString());

        return model;
    }

    /*public static CommonModel bindCommonData(JsonObject object) {

        CommonModel model = new CommonModel((!object.has("id") ? "" : object.get("id").getAsString()), (!object.has("name") ? "" : object.get("name").getAsString()), (!object.has("subTitle") ? "" : object.get("subTitle").getAsString()), (!object.has("image") ? "" : object.get("image").getAsString()), false);

        return model;
    }*/

    public static Product bindProductData(JsonObject object) {

        Product model = new Product();
        if (object.has("id")) model.setId(object.get("id").getAsString());
        if (object.has("name")) model.setName(object.get("name").getAsString());
        if (object.has("mrp")) model.setMrp(object.get("mrp").getAsString());
        if (object.has("price")) model.setPrice(object.get("price").getAsString());
        if (object.has("discount")) model.setDiscount(object.get("discount").getAsString());
        if (object.has("image")) model.setImage(object.get("image").getAsString());

        if (object.has("subcategory"))
            model.setSubcategory(object.get("subcategory").getAsString());
        if (object.has("category")) model.setCategory(object.get("category").getAsString());
        if (object.has("packing")) model.setPacking(object.get("packing").getAsString());
        if (object.has("tabStrip")) model.setTabStrip(object.get("tabStrip").getAsString());
        if (object.has("packingInfo"))
            model.setPackingInfo(object.get("packingInfo").getAsString());
        if (object.has("description"))
            model.setDescription(object.get("description").getAsString());
        if (object.has("specialNote"))
            model.setSpecialNote(object.get("specialNote").getAsString());
        if (object.has("hsnNo")) model.setHsnNo(object.get("hsnNo").getAsString());
        if (object.has("image2")) model.setImage2(object.get("image2").getAsString());
        if (object.has("image3")) model.setImage3(object.get("image3").getAsString());
        if (object.has("cgst")) model.setCgst(object.get("cgst").getAsString());
        if (object.has("sgst")) model.setSgst(object.get("sgst").getAsString());
        if (object.has("igst")) model.setIgst(object.get("igst").getAsString());
        if (object.has("expValInMonth"))
            model.setExpValInMonth(object.get("expValInMonth").getAsString());
        if (object.has("compGroup")) model.setCompGroup(object.get("compGroup").getAsString());
        if (object.has("prescriptionRequired"))
            model.setPrescriptionRequired(object.get("prescriptionRequired").getAsString());
        if (object.has("compName")) model.setCompName(object.get("compName").getAsString());
        if (object.has("altQty")) model.setAltQty(object.get("altQty").getAsString());
        if (object.has("usageDetails"))
            model.setUsageDetails(object.get("usageDetails").getAsString());
        if (object.has("indication")) model.setIndication(object.get("indication").getAsString());
        if (object.has("sideEffect")) model.setSideEffect(object.get("sideEffect").getAsString());
        if (object.has("warning")) model.setWarning(object.get("warning").getAsString());
        if (object.has("dTodIntrection"))
            model.setdTodIntrection(object.get("dTodIntrection").getAsString());
        if (object.has("phyLoc")) model.setPhyLoc(object.get("phyLoc").getAsString());
        if (object.has("bestSellingStatus"))
            model.setBestSellingStatus(object.get("bestSellingStatus").getAsString());
        if (object.has("status")) model.setStatus(object.get("status").getAsString());
        if (object.has("endUserVisibility"))
            model.setEndUserVisibility(object.get("endUserVisibility").getAsString());
        if (object.has("adminVerStatus"))
            model.setAdminVerStatus(object.get("adminVerStatus").getAsString());
        if (object.has("vendorId")) model.setVendorId(object.get("vendorId").getAsString());

        return model;
    }

    public static News bindNewsData(JsonObject object) {

        News model = new News();
        if (object.has("id"))
            model.setId(object.get("id").isJsonNull() ? "" : object.get("id").getAsString());
        if (object.has("title"))
            model.setTitle(object.get("title").isJsonNull() ? "" : object.get("title").getAsString());
        if (object.has("msg"))
            model.setMsg(object.get("msg").isJsonNull() ? "" : object.get("msg").getAsString());
        if (object.has("file"))
            model.setFile(object.get("file").isJsonNull() ? "" : object.get("file").getAsString());
        if (object.has("date"))
            model.setDate(object.get("date").isJsonNull() ? "" : object.get("date").getAsString());

        return model;
    }

    public static Gallery bindGalleryData(JsonObject object) {

        Gallery model = new Gallery();
        if (object.has("id"))
            model.setId(object.get("id").isJsonNull() ? "" : object.get("id").getAsString());
        if (object.has("events"))
            model.setEvents(object.get("events").isJsonNull() ? "" : object.get("events").getAsString());
        if (object.has("name"))
            model.setName(object.get("name").isJsonNull() ? "" : object.get("name").getAsString());
        if (object.has("img"))
            model.setImg(object.get("img").isJsonNull() ? "" : object.get("img").getAsString());

        return model;
    }

    public static Management bindManagementData(JsonObject object) {

        Management model = new Management();
        if (object.has("name"))
            model.setName(object.get("name").isJsonNull() ? "" : object.get("name").getAsString());
        if (object.has("designation"))
            model.setDesignation(object.get("designation").isJsonNull() ? "" : object.get("designation").getAsString());
        if (object.has("description"))
            model.setDescription(object.get("description").isJsonNull() ? "" : object.get("description").getAsString());
        if (object.has("img"))
            model.setImg(object.get("img").isJsonNull() ? "" : object.get("img").getAsString());

        return model;
    }

    public static Result bindResultData(JsonObject object) {

        Result model = new Result();
//        if (object.has("subject"))
//            model.setSubject(object.get("subject").isJsonNull() ? "" : object.get("subject").getAsString());
        if (object.has("type"))
            model.setType(object.get("type").isJsonNull() ? "" : object.get("type").getAsString());
        if (object.has("total"))
            model.setTotal_marks(object.get("total").isJsonNull() ? "" : object.get("total").getAsString());
        if (object.has("marks"))
            model.setObtained_marks(object.get("marks").isJsonNull() ? "" : object.get("marks").getAsString());
        if (object.has("percentage"))
            model.setPercentage(object.get("percentage").isJsonNull() ? "" : object.get("percentage").getAsString());
        if (object.has("date"))
            model.setDate(object.get("date").isJsonNull() ? "" : object.get("date").getAsString());
        if (object.has("testname"))
            model.setTestname(object.get("testname").isJsonNull() ? "" : object.get("testname").getAsString());
        if (object.has("total"))
            model.setTotal(object.get("total").isJsonNull() ? "" : object.get("total").getAsString());
        if (object.has("trank"))
            model.setTrank(object.get("trank").isJsonNull() ? "" : object.get("trank").getAsString());
        // Parse arrays
        if (object.has("subject")) {
            model.setListSubject(parseStringArray(object.getAsJsonArray("subject")));
        }
        if (object.has("mark")) {
            model.setMark(parseStringArray(object.getAsJsonArray("mark")));
        }
        if (object.has("subtotal")) {
            model.setSubtotal(parseStringArray(object.getAsJsonArray("subtotal")));
        }
        if (object.has("subrank")) {
            model.setSubrank(parseStringArray(object.getAsJsonArray("subrank")));
        }

        return model;
    }

    private static List<String> parseStringArray(JsonArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            list.add(element.getAsString());
        }
        return list;
    }

    public static Review bindReviewData(JsonObject object) {

        Review model = new Review();
        if (object.has("review"))
            model.setReview(object.get("review").isJsonNull() ? "" : object.get("review").getAsString());
        if (object.has("date"))
            model.setDate(object.get("date").isJsonNull() ? "" : object.get("date").getAsString());
        return model;
    }

   /* public static Attendance bindAttendanceData(JsonObject jsonObject) {
        Attendance attendance = new Attendance();

        String monthName = jsonObject.keySet().iterator().next(); // Get the month name
        MonthData monthData = parseMonthData(jsonObject.getAsJsonObject(monthName));
            switch (monthName) {
                case "jun":
                    attendance.setJun(monthData);
                    break;
                case "jul":
                    attendance.setJul(monthData);
                    break;
                case "aug":
                    attendance.setAug(monthData);
                    break;
                case "sep":
                    attendance.setSep(monthData);
                    break;
                case "oct":
                    attendance.setOct(monthData);
                    break;
                case "nov":
                    attendance.setNov(monthData);
                    break;
                case "dec":
                    attendance.setDec(monthData);
                    break;
                case "jan":
                    attendance.setJan(monthData);
                    break;
                case "feb":
                    attendance.setFeb(monthData);
                    break;
                case "mar":
                    attendance.setMar(monthData);
                    break;
                case "apr":
                    attendance.setApr(monthData);
                    break;
                case "may":
                    attendance.setMay(monthData);
                    break;
                default:
                    break;
            }

        return attendance;
    }*/

    public static Attendance bindAttendanceData(JsonObject jsonObject) {
        Attendance attendance = new Attendance();

        for (String monthName : jsonObject.keySet()) {
            MonthData monthData = parseMonthData(jsonObject.getAsJsonObject(monthName),monthName);
            setMonthData(attendance, monthName, monthData);
        }

        return attendance;
    }

    private static void setMonthData(Attendance attendance, String monthName, MonthData monthData) {
        switch (monthName) {
            case "jun":
                attendance.setJun(monthData);
                break;
            case "jul":
                attendance.setJul(monthData);
                break;
            case "aug":
                attendance.setAug(monthData);
                break;
            case "sep":
                attendance.setSep(monthData);
                break;
            case "oct":
                attendance.setOct(monthData);
                break;
            case "nov":
                attendance.setNov(monthData);
                break;
            case "dec":
                attendance.setDec(monthData);
                break;
            case "jan":
                attendance.setJan(monthData);
                break;
            case "feb":
                attendance.setFeb(monthData);
                break;
            case "mar":
                attendance.setMar(monthData);
                break;
            case "apr":
                attendance.setApr(monthData);
                break;
            case "may":
                attendance.setMay(monthData);
                break;
            default: // Handle unexpected month names
        }
    }

    private static MonthData parseMonthData(JsonObject monthObject, String monthName) {
        MonthData monthData = new MonthData();

        for (int day = 1; day <= 33; day++) {
            String dayKey = (day < 10) ? "0" + day : String.valueOf(day);

            if (dayKey.equals("32")) {
                dayKey = "total";
            } else if (dayKey.equals("33")) {
                String month = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
                dayKey = "month" + month;
            }
            if (monthObject.has(dayKey)) {
                // Set day-wise data
                switch (day) {
                    case 32:
                        monthData.setTotal(monthObject.get(dayKey).getAsString());
                        break;
                    case 33:
                        monthData.setMonth(monthObject.get(dayKey).getAsString());
                        break;

                    default:
                        setDayData(monthData, "setDay" + day, monthObject.get(dayKey).getAsString());
                        break;
                }
            }
        }

        return monthData;
    }

    private static void setDayData(MonthData monthData, String methodName, String value) {
        try {
            // Use reflection to dynamically call the setDayX method based on the day
            MonthData.class.getMethod(methodName, String.class).invoke(monthData, value);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle reflection or method invocation exceptions
        }
    }

    public static Subject bindSubjectData(JsonObject object) {

        Subject model = new Subject();
        if (object.has("sub_id"))
            model.setId(object.get("sub_id").isJsonNull() ? "" : object.get("sub_id").getAsString());
        if (object.has("subject"))
            model.setSubject(object.get("subject").isJsonNull() ? "" : object.get("subject").getAsString());
        if (object.has("type"))
            model.setType(object.get("type").isJsonNull() ? "" : object.get("type").getAsString());
        if (object.has("title"))
            model.setTitle(object.get("title").isJsonNull() ? "" : object.get("title").getAsString());
        if (object.has("vid_code"))
            model.setVidCode(object.get("vid_code").isJsonNull() ? "" : object.get("vid_code").getAsString());

        return model;
    }

    public static Notification bindNotificationData(JsonObject object) {

        Notification model = new Notification();
        if (object.has("title"))
            model.setTitle(object.get("title").isJsonNull() ? "" : object.get("title").getAsString());
        if (object.has("desc"))
            model.setDesc(object.get("desc").isJsonNull() ? "" : object.get("desc").getAsString());
        if (object.has("type"))
            model.setType(object.get("type").isJsonNull() ? "" : object.get("type").getAsString());

        return model;
    }
}
