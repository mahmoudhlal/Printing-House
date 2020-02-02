package com.mahmoud.printinghouse.models.orderResponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.SerializedName;

public class OrdersItem{

	@SerializedName("file_path")
	private String filePath;

	@SerializedName("shield_type")
	private String shieldType;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("print_type")
	private String printType;

	@SerializedName("type")
	private String type;

	@SerializedName("content")
	private String content;

	@SerializedName("paper_type")
	private String paperType;

	@SerializedName("copies")
	private String copies;

	@SerializedName("paper_size")
	private String paperSize;

	@SerializedName("location")
	private Location location;

	@SerializedName("id")
	private int id;

	@SerializedName("user")
	private User user;

	@SerializedName("shield_image")
	private String shieldImage;

	public void setFilePath(String filePath){
		this.filePath = filePath;
	}

	public String getFilePath(){
		return filePath;
	}

	public void setShieldType(String shieldType){
		this.shieldType = shieldType;
	}

	public String getShieldType(){
		return shieldType;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPrintType(String printType){
		this.printType = printType;
	}

	public String getPrintType(){
		return printType;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setPaperType(String paperType){
		this.paperType = paperType;
	}

	public String getPaperType(){
		return paperType;
	}

	public void setCopies(String copies){
		this.copies = copies;
	}

	public String getCopies(){
		return copies;
	}

	public void setPaperSize(String paperSize){
		this.paperSize = paperSize;
	}

	public String getPaperSize(){
		return paperSize;
	}

	public void setLocation(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return location;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setShieldImage(String shieldImage){
		this.shieldImage = shieldImage;
	}

	public String getShieldImage(){
		return shieldImage;
	}


	public static final DiffUtil.ItemCallback<OrdersItem> DIFF_CALLBACK =
			new DiffUtil.ItemCallback<OrdersItem>() {
		@Override
		public boolean areItemsTheSame(OrdersItem oldItem, OrdersItem newItem) {
			return oldItem.getId() == newItem.getId();
		}

		@Override
		public boolean areContentsTheSame(OrdersItem oldItem, @NonNull OrdersItem newItem) {
			return oldItem.equals(newItem);
		}
	};


	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		return filePath.equals(((OrdersItem) obj).filePath)
				&& shieldType.equals(((OrdersItem) obj).shieldType)
				&& createdAt.equals(((OrdersItem) obj).createdAt)
				&& printType.equals(((OrdersItem) obj).printType)
				&& type.equals(((OrdersItem) obj).type)
				&& content.equals(((OrdersItem) obj).content)
				&& id==((OrdersItem) obj).id
				&& paperSize.equals(((OrdersItem) obj).paperSize)
				&& paperType.equals(((OrdersItem) obj).paperType)
				&& copies.equals(((OrdersItem) obj).copies)
				&& shieldImage.equals(((OrdersItem) obj).shieldImage)
				&& location.equals(((OrdersItem) obj).location)
				&& user.equals(((OrdersItem) obj).user);
	}

}