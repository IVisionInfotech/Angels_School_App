package com.ivision.model;

import java.io.Serializable;

public class Product implements Serializable {

    private String id, name, mrp, price, discount, image;
    private String subcategory, category, packing, tabStrip, packingInfo, description, specialNote, hsnNo, image2, image3, cgst, sgst, igst,
            expValInMonth, compGroup, prescriptionRequired, compName, altQty, usageDetails, indication, sideEffect, warning, dTodIntrection, phyLoc,
            bestSellingStatus, status, endUserVisibility, adminVerStatus, vendorId;
    private String cartQuantity, cartPrice, cartPriceTotal;
    private boolean selected;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getTabStrip() {
        return tabStrip;
    }

    public void setTabStrip(String tabStrip) {
        this.tabStrip = tabStrip;
    }

    public String getPackingInfo() {
        return packingInfo;
    }

    public void setPackingInfo(String packingInfo) {
        this.packingInfo = packingInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public String getHsnNo() {
        return hsnNo;
    }

    public void setHsnNo(String hsnNo) {
        this.hsnNo = hsnNo;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getExpValInMonth() {
        return expValInMonth;
    }

    public void setExpValInMonth(String expValInMonth) {
        this.expValInMonth = expValInMonth;
    }

    public String getCompGroup() {
        return compGroup;
    }

    public void setCompGroup(String compGroup) {
        this.compGroup = compGroup;
    }

    public String getPrescriptionRequired() {
        return prescriptionRequired;
    }

    public void setPrescriptionRequired(String prescriptionRequired) {
        this.prescriptionRequired = prescriptionRequired;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getAltQty() {
        return altQty;
    }

    public void setAltQty(String altQty) {
        this.altQty = altQty;
    }

    public String getUsageDetails() {
        return usageDetails;
    }

    public void setUsageDetails(String usageDetails) {
        this.usageDetails = usageDetails;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getSideEffect() {
        return sideEffect;
    }

    public void setSideEffect(String sideEffect) {
        this.sideEffect = sideEffect;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getdTodIntrection() {
        return dTodIntrection;
    }

    public void setdTodIntrection(String dTodIntrection) {
        this.dTodIntrection = dTodIntrection;
    }

    public String getPhyLoc() {
        return phyLoc;
    }

    public void setPhyLoc(String phyLoc) {
        this.phyLoc = phyLoc;
    }

    public String getBestSellingStatus() {
        return bestSellingStatus;
    }

    public void setBestSellingStatus(String bestSellingStatus) {
        this.bestSellingStatus = bestSellingStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndUserVisibility() {
        return endUserVisibility;
    }

    public void setEndUserVisibility(String endUserVisibility) {
        this.endUserVisibility = endUserVisibility;
    }

    public String getAdminVerStatus() {
        return adminVerStatus;
    }

    public void setAdminVerStatus(String adminVerStatus) {
        this.adminVerStatus = adminVerStatus;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(String cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public String getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(String cartPrice) {
        this.cartPrice = cartPrice;
    }

    public String getCartPriceTotal() {
        return cartPriceTotal;
    }

    public void setCartPriceTotal(String cartPriceTotal) {
        this.cartPriceTotal = cartPriceTotal;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
