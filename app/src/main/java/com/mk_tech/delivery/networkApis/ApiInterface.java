package com.mk_tech.delivery.networkApis;

//___________________________________Referance____________________________________
// https://kmangutov.wordpress.com/2015/03/28/android-mvp-consuming-restful-apis/
//___________________________________Referance____________________________________


import com.mk_tech.delivery.model.AddressGlobalModel;
import com.mk_tech.delivery.model.AddressListModel;
import com.mk_tech.delivery.model.AddressModel;
import com.mk_tech.delivery.model.AreaModel;
import com.mk_tech.delivery.model.CartModel;
import com.mk_tech.delivery.model.CityModel;
import com.mk_tech.delivery.model.CompaniesModel;
import com.mk_tech.delivery.model.Company;
import com.mk_tech.delivery.model.CompanyDetailsModel;
import com.mk_tech.delivery.model.DeliveryPriceModel;
import com.mk_tech.delivery.model.ForgetPasswordModel;
import com.mk_tech.delivery.model.HomeModel;
import com.mk_tech.delivery.model.OrderDetailsModel;
import com.mk_tech.delivery.model.OrdersModel;
import com.mk_tech.delivery.model.PaymentCashModel;
import com.mk_tech.delivery.model.PaymentMethodModel;
import com.mk_tech.delivery.model.PaymentModel;
import com.mk_tech.delivery.model.Product;
import com.mk_tech.delivery.model.ProductModel;
import com.mk_tech.delivery.model.ProductsModel;
import com.mk_tech.delivery.model.ProfileUpdateModel;
import com.mk_tech.delivery.model.RegisterModel;
import com.mk_tech.delivery.model.SearchKeywordsModel;
import com.mk_tech.delivery.model.SearchModel;
import com.mk_tech.delivery.model.TearmsModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiInterface {
    @GET("home")
    Observable<HomeModel> getHome();


    @GET("products/view/{id}")
    Observable<ProductModel> getProductDetails(@Path("id") int id);



    @GET("products/{type}")
    Observable<ProductsModel> getProducts(@Path("type") String type, @Query("page") int page);

    @GET("{type}")
    Observable<TearmsModel> getData(@Path("type") String type);



    @GET("stores/products/sections/{id}")
    Observable<ProductsModel> getStoreProductBySection(@Path("id") int id);

    @GET("companies/categories/products/{id}")
    Observable<ProductsModel> getCompaniesProductsByCategories(@Path("id") int id, @Query("page") int page);


    @GET("companies/special_offers/{id}")
    Observable<ProductsModel> getCompaniesOffers(@Path("id") int id, @Query("page") int page);


    @GET("companies/categories/products/{id}")
    Observable<CompanyDetailsModel> getBrandsProductsByCategories(@Path("id") int id, @Query("page") int page);



    @POST("companies/filter/{ID}")
    @FormUrlEncoded
    Observable<CompanyDetailsModel> CompaniesFilter(@Path("ID") int ID,
                                                    @Field("search") String search,
                                                    @Field("min") String min,
                                                    @Field("max") String max,
                                                    @Field("price_sort") String price_sort,
                                                    @Query("page") int page);




    @GET("companies/view/{ID}")
    Observable<CompanyDetailsModel> getCompaniesDetails(@Path("ID") int ID, @Query("page") int page);


    @GET("brands/view/{id}")
    Observable<CompanyDetailsModel> getBrandDetails(@Path("id") int id, @Query("page") int page);


    @GET("companies")
    Observable<CompaniesModel> getCompanies(@Query("page") int page);

    @GET("companies/types/{type}")
    Observable<CompaniesModel> getCompaniesByType(@Path("type") String type, @Query("page") int page);

    @GET("companies/types/categories/{id}")
    Observable<CompaniesModel> getCompaniesByCategory(@Path("id") int id, @Query("page") int page);

    @GET("workshops/category/{id}")
    Observable<CompaniesModel> getCompaniesByWorkshop(@Path("id") int id, @Query("page") int page);


    @GET("wishlists")
    Observable<ProductsModel> getWishlist(@Query("page") int page);

     @GET("users/profile")
    Observable<ProfileUpdateModel> getProfile();

    @POST("wishlists/toggle")
    @FormUrlEncoded
    Observable<ProductModel> wishlistsAddRemove(@Field("product_id") int product_id);


    @POST("search/{type}")
    @FormUrlEncoded
    Observable<SearchModel<Product>> searchProduct(@Path("type") String type,
                                                   @Field("search") String word, @Query("page") int page);



    @POST("search/keywords")
    @FormUrlEncoded
    Observable<SearchKeywordsModel> searchKeywords(@Field("keyword") String keyword,
                                                   @Field("type") String type);

    @POST("search/{type}")
    @FormUrlEncoded
    Observable<SearchModel<Company>> searchCompany(@Path("type") String type,
                                                   @Field("search") String word, @Query("page") int page);

    @POST("auth/forgot_password")
    @FormUrlEncoded
    Observable<ForgetPasswordModel> forgot_password(@Field("email") String email);


    @GET("carts")
    Observable<CartModel> getCarts();

    @GET("carts/promo_code")
    Observable<CartModel> getPromo_code();

    @GET("notifications")
    Observable<CartModel> getNotifications();

    @POST("carts/create")
    @FormUrlEncoded
    Observable<CartModel> cartsCreate(@Field("product_id") int product_id,
                                      @Field("quantity") int quantity,
                                      @Field("celebrity_id") String celebrity_id);

    @POST("carts/delete")
    @FormUrlEncoded
    Observable<CartModel> CartProductDelete(@Field("cart_product_id") int cart_product_id);

    @POST("carts/update")
    @FormUrlEncoded
    Observable<CartModel> cartsUpdate(@Field("cart_product_id") int cart_product_id, @Field("quantity") int quantity);


    @POST("auth/register")
    @FormUrlEncoded
    Observable<RegisterModel> Register(@Field("name") String name,
                                       @Field("email") String email,
                                       @Field("mobile") String mobile,
                                       @Field("password") String password,
                                       @Field("gender") String gender,
                                       @Field("device_type") String device_type,
                                       @Field("fcm_token") String fcm_token);

    @Multipart
    @POST("users/update")
    Observable<ProfileUpdateModel> profileUpdate(@Part("name") RequestBody name,
                                                 @Part("email") RequestBody email,
                                                 @Part("mobile") RequestBody mobile,
                                                 @Part MultipartBody.Part file);

    @POST("auth/new_password")
    @FormUrlEncoded
    Observable<RegisterModel> changeForgetPassword(@Field("code") String code,
                                                   @Field("email") String email,
                                                   @Field("new_password") String new_password);

    @POST("users/update_password")
    @FormUrlEncoded
    Observable<ProfileUpdateModel> updatePassword(@Field("old_password") String old_password,
                                                  @Field("new_password") String new_password);


    @POST("auth/login")
    @FormUrlEncoded
    Observable<RegisterModel> Login(@Field("email") String email,
                                    @Field("password") String password,
                                    @Field("device_type") String device_type,
                                    @Field("fcm_token") String fcm_token);




    @POST("user_occasions/create")
    @FormUrlEncoded
    Observable<AddressModel> OccasionsAdd(
            @Field("name") String name,
            @Field("type") String type,
            @Field("date") String date,
            @Field("reminder") int reminder);

    @POST("user_occasions/update")
    @FormUrlEncoded
    Observable<AddressModel> OccasionsUpdate(@Field("id") int id,
                                             @Field("name") String name,
                                             @Field("type") String type,
                                             @Field("date") String date,
                                             @Field("reminder") int reminder);


    @GET("addresses/cities")
    Observable<CityModel> getCities();

    @GET("addresses/areas/{ID}")
    Observable<AreaModel> getAreas(@Path("ID") int ID);

    @GET("addresses")
    Observable<AddressListModel> getAddresses();

    @GET("checkout/payment_methods")
    Observable<PaymentMethodModel> getPaymentMethods();




    @GET("addresses/global")
    Observable<AddressGlobalModel> getAddressesGlobal();

    @GET("orders")
    Observable<OrdersModel> getOrders();

    @GET("orders/view/{ID}")
    Observable<OrderDetailsModel> getOrderDetails(@Path("ID") Integer ID);



    @POST("addresses/create")
    @FormUrlEncoded
    Observable<AddressModel> createAddresses(
            @Field("city_id") String city_id,
            @Field("area_id") String area_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") String phone,
            @Field("block") String block,
            @Field("street") String street,
            @Field("building") String building,
            @Field("apartment_no") String apartment_no,
            @Field("avenue") String avenue,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @POST("addresses/update")
    @FormUrlEncoded
    Observable<AddressModel> updateAddresses(
            @Field("address_id") int address_id,
            @Field("area_id") String area_id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("block") String block,
            @Field("street") String street,
            @Field("building") String building,
            @Field("apartment_no") String apartment_no,
            @Field("avenue") String avenue,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude

    );

    @POST("addresses/delete")
    @FormUrlEncoded
    Observable<AddressModel> deleteAddresses(
            @Field("address_id") int address_id

    );


    @POST("checkout/payment/details")
    @FormUrlEncoded
    Observable<DeliveryPriceModel> getDeliveryPrice(@Field("address_id") Integer address_id);

    @POST("checkout/payment")
    @FormUrlEncoded
    Observable<PaymentModel> getPayment(@Field("address_id") Integer address_id, @Field("payment_method") String payment_method);

    @POST("checkout/payment")
    @FormUrlEncoded
    Observable<PaymentCashModel> getPaymentCash(@Field("is_global") Integer is_global, @Field("address_id") Integer address_id, @Field("payment_method") String payment_method);

}

