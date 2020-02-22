package com.nettlike.app.model.dialog;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DialogModel {
        @SerializedName("ok")
        private Boolean ok;
        @SerializedName("payload")
        private DiaPayload payload;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public DiaPayload getPayload() {
        return payload;
    }

    public void setPayload(DiaPayload payload) {
        this.payload = payload;
    }



    private class Info{
        @SerializedName("is_activation_email_sent")
        private Boolean isActivationEmailSent;
        @SerializedName("lang")
        private String lang;
        @SerializedName("location")
        private DialogModel.Location location;
        @SerializedName("utm_data")
        private UtmData utmData;
        @SerializedName("welcome_email_sent")
        private Boolean welcomeEmailSent;

        public Boolean getActivationEmailSent() {
            return isActivationEmailSent;
        }

        public void setActivationEmailSent(Boolean activationEmailSent) {
            isActivationEmailSent = activationEmailSent;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public UtmData getUtmData() {
            return utmData;
        }

        public void setUtmData(UtmData utmData) {
            this.utmData = utmData;
        }

        public Boolean getWelcomeEmailSent() {
            return welcomeEmailSent;
        }

        public void setWelcomeEmailSent(Boolean welcomeEmailSent) {
            this.welcomeEmailSent = welcomeEmailSent;
        }
    }

    private class LastMessage{
        @SerializedName("_id")
        private String id;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("created_by")
        private String createdBy;
        @SerializedName("dialog_id")
        private String dialogId;
        @SerializedName("seen_by")
        private List<String> seenBy = null;
        @SerializedName("text")
        private String text;
        @SerializedName("type")
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getDialogId() {
            return dialogId;
        }

        public void setDialogId(String dialogId) {
            this.dialogId = dialogId;
        }

        public List<String> getSeenBy() {
            return seenBy;
        }

        public void setSeenBy(List<String> seenBy) {
            this.seenBy = seenBy;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private class Location{
        @SerializedName("calling_code")
        private Object callingCode;
        @SerializedName("city")
        private Object city;
        @SerializedName("country_code")
        private Object countryCode;
        @SerializedName("country_flag")
        private Object countryFlag;
        @SerializedName("country_flag_emoji")
        private Object countryFlagEmoji;
        @SerializedName("country_name")
        private Object countryName;
        @SerializedName("language")
        private String language;
        @SerializedName("zip")
        private Object zip;

        public Object getCallingCode() {
            return callingCode;
        }

        public void setCallingCode(Object callingCode) {
            this.callingCode = callingCode;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(Object countryCode) {
            this.countryCode = countryCode;
        }

        public Object getCountryFlag() {
            return countryFlag;
        }

        public void setCountryFlag(Object countryFlag) {
            this.countryFlag = countryFlag;
        }

        public Object getCountryFlagEmoji() {
            return countryFlagEmoji;
        }

        public void setCountryFlagEmoji(Object countryFlagEmoji) {
            this.countryFlagEmoji = countryFlagEmoji;
        }

        public Object getCountryName() {
            return countryName;
        }

        public void setCountryName(Object countryName) {
            this.countryName = countryName;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public Object getZip() {
            return zip;
        }

        public void setZip(Object zip) {
            this.zip = zip;
        }
    }

    private class Member{
        @SerializedName("_id")
        private String id;
        @SerializedName("activation_code")
        private String activationCode;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("email")
        private String email;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("followers")
        private List<String> followers = null;
        @SerializedName("following")
        private List<String> following = null;
        @SerializedName("gender")
        private String gender;
        @SerializedName("hidden_questions")
        private List<Object> hiddenQuestions = null;
        @SerializedName("info")
        private Info info;
        @SerializedName("invite_code")
        private String inviteCode;
        @SerializedName("is_admin")
        private Boolean isAdmin;
        @SerializedName("lang")
        private String lang;
        @SerializedName("last_name")
        private String lastName;
        @SerializedName("patronymic_name")
        private String patronymicName;
        @SerializedName("phone")
        private String phone;
        @SerializedName("saved_people")
        private List<String> savedPeople = null;
        @SerializedName("saved_questions")
        private List<String> savedQuestions = null;
        @SerializedName("status")
        private String status;
        @SerializedName("mapTags")
        private List<Tag> tags = null;
        @SerializedName("updated_at")
        private String updatedAt;
        @SerializedName("apple_device_token")
        private String appleDeviceToken;
        @SerializedName("apple_id")
        private String appleId;
        @SerializedName("birth_year")
        private String birthYear;
        @SerializedName("city")
        private String city;
        @SerializedName("country")
        private String country;
        @SerializedName("description")
        private String description;
        @SerializedName("facebook_url")
        private String facebookUrl;
        @SerializedName("github_url")
        private String githubUrl;
        @SerializedName("job_title")
        private String jobTitle;
        @SerializedName("picture_id")
        private String pictureId;
        @SerializedName("twitter_url")
        private String twitterUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getActivationCode() {
            return activationCode;
        }

        public void setActivationCode(String activationCode) {
            this.activationCode = activationCode;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public List<String> getFollowers() {
            return followers;
        }

        public void setFollowers(List<String> followers) {
            this.followers = followers;
        }

        public List<String> getFollowing() {
            return following;
        }

        public void setFollowing(List<String> following) {
            this.following = following;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public List<Object> getHiddenQuestions() {
            return hiddenQuestions;
        }

        public void setHiddenQuestions(List<Object> hiddenQuestions) {
            this.hiddenQuestions = hiddenQuestions;
        }

        public Info getInfo() {
            return info;
        }

        public void setInfo(Info info) {
            this.info = info;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public Boolean getAdmin() {
            return isAdmin;
        }

        public void setAdmin(Boolean admin) {
            isAdmin = admin;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPatronymicName() {
            return patronymicName;
        }

        public void setPatronymicName(String patronymicName) {
            this.patronymicName = patronymicName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<String> getSavedPeople() {
            return savedPeople;
        }

        public void setSavedPeople(List<String> savedPeople) {
            this.savedPeople = savedPeople;
        }

        public List<String> getSavedQuestions() {
            return savedQuestions;
        }

        public void setSavedQuestions(List<String> savedQuestions) {
            this.savedQuestions = savedQuestions;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getAppleDeviceToken() {
            return appleDeviceToken;
        }

        public void setAppleDeviceToken(String appleDeviceToken) {
            this.appleDeviceToken = appleDeviceToken;
        }

        public String getAppleId() {
            return appleId;
        }

        public void setAppleId(String appleId) {
            this.appleId = appleId;
        }

        public String getBirthYear() {
            return birthYear;
        }

        public void setBirthYear(String birthYear) {
            this.birthYear = birthYear;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFacebookUrl() {
            return facebookUrl;
        }

        public void setFacebookUrl(String facebookUrl) {
            this.facebookUrl = facebookUrl;
        }

        public String getGithubUrl() {
            return githubUrl;
        }

        public void setGithubUrl(String githubUrl) {
            this.githubUrl = githubUrl;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }

        public String getPictureId() {
            return pictureId;
        }

        public void setPictureId(String pictureId) {
            this.pictureId = pictureId;
        }

        public String getTwitterUrl() {
            return twitterUrl;
        }

        public void setTwitterUrl(String twitterUrl) {
            this.twitterUrl = twitterUrl;
        }
    }
    public class Payload{
        @SerializedName("_id")
        private String id;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("created_by")
        private String createdBy;
        @SerializedName("last_message")
        private LastMessage lastMessage;
        @SerializedName("members")
        private List<Member> members = null;
        @SerializedName("status")
        private String status;
        @SerializedName("type")
        private String type;
        @SerializedName("updated_at")
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public LastMessage getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(LastMessage lastMessage) {
            this.lastMessage = lastMessage;
        }

        public List<Member> getMembers() {
            return members;
        }

        public void setMembers(List<Member> members) {
            this.members = members;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
    private class Tag{
        @SerializedName("_id")
        private String id;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("emoji")
        private String emoji;
        @SerializedName("lang")
        private String lang;
        @SerializedName("name")
        private String name;
        @SerializedName("order")
        private Integer order;
        @SerializedName("parent_ids")
        private List<Object> parentIds = null;
        @SerializedName("status")
        private String status;
        @SerializedName("updated_at")
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getEmoji() {
            return emoji;
        }

        public void setEmoji(String emoji) {
            this.emoji = emoji;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public List<Object> getParentIds() {
            return parentIds;
        }

        public void setParentIds(List<Object> parentIds) {
            this.parentIds = parentIds;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
    private class UtmData{
        @SerializedName("utm_campaign")
        private Object utmCampaign;
        @SerializedName("utm_content")
        private Object utmContent;
        @SerializedName("utm_medium")
        private Object utmMedium;
        @SerializedName("utm_source")
        private Object utmSource;
        @SerializedName("utm_term")
        private Object utmTerm;

        public Object getUtmCampaign() {
            return utmCampaign;
        }

        public void setUtmCampaign(Object utmCampaign) {
            this.utmCampaign = utmCampaign;
        }

        public Object getUtmContent() {
            return utmContent;
        }

        public void setUtmContent(Object utmContent) {
            this.utmContent = utmContent;
        }

        public Object getUtmMedium() {
            return utmMedium;
        }

        public void setUtmMedium(Object utmMedium) {
            this.utmMedium = utmMedium;
        }

        public Object getUtmSource() {
            return utmSource;
        }

        public void setUtmSource(Object utmSource) {
            this.utmSource = utmSource;
        }

        public Object getUtmTerm() {
            return utmTerm;
        }

        public void setUtmTerm(Object utmTerm) {
            this.utmTerm = utmTerm;
        }
    }
}
