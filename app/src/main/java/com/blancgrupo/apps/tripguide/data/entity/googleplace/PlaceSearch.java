
package com.blancgrupo.apps.tripguide.data.entity.googleplace;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceSearch implements Parcelable
{

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("id")
    @Expose
    private String id;
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
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    public final static Creator<PlaceSearch> CREATOR = new Creator<PlaceSearch>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PlaceSearch createFromParcel(Parcel in) {
            PlaceSearch instance = new PlaceSearch();
            instance.formattedAddress = ((String) in.readValue((String.class.getClassLoader())));
            instance.geometry = ((Geometry) in.readValue((Geometry.class.getClassLoader())));
            instance.icon = ((String) in.readValue((String.class.getClassLoader())));
            instance.id = ((String) in.readValue((String.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.openingHours = ((OpeningHours) in.readValue((OpeningHours.class.getClassLoader())));
            in.readList(instance.photos, (Photo.class.getClassLoader()));
            instance.placeId = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.reference = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.types, (String.class.getClassLoader()));
            return instance;
        }

        public PlaceSearch[] newArray(int size) {
            return (new PlaceSearch[size]);
        }

    }
    ;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PlaceSearch() {
    }

    /**
     * 
     * @param photos
     * @param id
     * @param icon
     * @param placeId
     * @param openingHours
     * @param name
     * @param formattedAddress
     * @param rating
     * @param types
     * @param reference
     * @param geometry
     */
    public PlaceSearch(String formattedAddress, Geometry geometry, String icon, String id, String name, OpeningHours openingHours, List<Photo> photos, String placeId, Double rating, String reference, List<String> types) {
        super();
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.openingHours = openingHours;
        this.photos = photos;
        this.placeId = placeId;
        this.rating = rating;
        this.reference = reference;
        this.types = types;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
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

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(formattedAddress);
        dest.writeValue(geometry);
        dest.writeValue(icon);
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(openingHours);
        dest.writeList(photos);
        dest.writeValue(placeId);
        dest.writeValue(rating);
        dest.writeValue(reference);
        dest.writeList(types);
    }

    public int describeContents() {
        return  0;
    }

}
