
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceDetail implements Parcelable
{

    @SerializedName("address_components")
    @Expose
    private List<AddressComponent> addressComponents = null;
    @SerializedName("adr_address")
    @Expose
    private String adrAddress;
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("international_phone_number")
    @Expose
    private String internationalPhoneNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;
    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("utc_offset")
    @Expose
    private Integer utcOffset;
    @SerializedName("vicinity")
    @Expose
    private String vicinity;
    @SerializedName("website")
    @Expose
    private String website;
    public final static Creator<PlaceDetail> CREATOR = new Creator<PlaceDetail>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PlaceDetail createFromParcel(Parcel in) {
            PlaceDetail instance = new PlaceDetail();
            in.readList(instance.addressComponents, (AddressComponent.class.getClassLoader()));
            instance.adrAddress = ((String) in.readValue((String.class.getClassLoader())));
            instance.formattedAddress = ((String) in.readValue((String.class.getClassLoader())));
            instance.formattedPhoneNumber = ((String) in.readValue((String.class.getClassLoader())));
            instance.geometry = ((Geometry) in.readValue((Geometry.class.getClassLoader())));
            instance.icon = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.internationalPhoneNumber = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.openingHours = ((OpeningHours) in.readValue((OpeningHours.class.getClassLoader())));
            in.readList(instance.photos, (Photo.class.getClassLoader()));
            instance.placeId = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.reference = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.reviews, (Review.class.getClassLoader()));
            instance.scope = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.types, (String.class.getClassLoader()));
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            instance.utcOffset = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.vicinity = ((String) in.readValue((String.class.getClassLoader())));
            instance.website = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public PlaceDetail[] newArray(int size) {
            return (new PlaceDetail[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PlaceDetail() {
    }

    /**
     * 
     * @param icon
     * @param reviews
     * @param scope
     * @param website
     * @param openingHours
     * @param adrAddress
     * @param url
     * @param reference
     * @param geometry
     * @param internationalPhoneNumber
     * @param id
     * @param photos
     * @param vicinity
     * @param formattedPhoneNumber
     * @param placeId
     * @param name
     * @param utcOffset
     * @param formattedAddress
     * @param rating
     * @param types
     * @param addressComponents
     */
    public PlaceDetail(List<AddressComponent> addressComponents, String adrAddress, String formattedAddress, String formattedPhoneNumber, Geometry geometry, String icon, String id, String internationalPhoneNumber, String name, OpeningHours openingHours, List<Photo> photos, String placeId, Double rating, String reference, List<Review> reviews, String scope, List<String> types, String url, Integer utcOffset, String vicinity, String website) {
        super();
        this.addressComponents = addressComponents;
        this.adrAddress = adrAddress;
        this.formattedAddress = formattedAddress;
        this.formattedPhoneNumber = formattedPhoneNumber;
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.internationalPhoneNumber = internationalPhoneNumber;
        this.name = name;
        this.openingHours = openingHours;
        this.photos = photos;
        this.placeId = placeId;
        this.rating = rating;
        this.reference = reference;
        this.reviews = reviews;
        this.scope = scope;
        this.types = types;
        this.url = url;
        this.utcOffset = utcOffset;
        this.vicinity = vicinity;
        this.website = website;
    }

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getAdrAddress() {
        return adrAddress;
    }

    public void setAdrAddress(String adrAddress) {
        this.adrAddress = adrAddress;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(Integer utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(addressComponents);
        dest.writeValue(adrAddress);
        dest.writeValue(formattedAddress);
        dest.writeValue(formattedPhoneNumber);
        dest.writeValue(geometry);
        dest.writeValue(icon);
        dest.writeValue(id);
        dest.writeValue(internationalPhoneNumber);
        dest.writeValue(name);
        dest.writeValue(openingHours);
        dest.writeList(photos);
        dest.writeValue(placeId);
        dest.writeValue(rating);
        dest.writeValue(reference);
        dest.writeList(reviews);
        dest.writeValue(scope);
        dest.writeList(types);
        dest.writeValue(url);
        dest.writeValue(utcOffset);
        dest.writeValue(vicinity);
        dest.writeValue(website);
    }

    public int describeContents() {
        return  0;
    }

}
