package org.trinityfforce.sagopalgo.common;

import java.time.LocalDate;
import org.trinityfforce.sagopalgo.category.entity.Category;
import org.trinityfforce.sagopalgo.item.entity.Item;
import org.trinityfforce.sagopalgo.item.entity.ItemStatusEnum;
import org.trinityfforce.sagopalgo.user.entity.SocialType;
import org.trinityfforce.sagopalgo.user.entity.User;
import org.trinityfforce.sagopalgo.user.entity.UserRoleEnum;

public class TestValue {

    public final static Long TEST_USER_ID1 = 1L;
    public final static String TEST_USERNAME1 = "user1";
    public final static String TEST_EMAIL1 = "test1@test.com";
    public final static String TEST_PASSWORD1 = "password1";
    public final static UserRoleEnum TEST_ROLE1 = UserRoleEnum.USER;
    public final static SocialType TEST_TYPE1 = SocialType.KAKAO;
    public final static String TEST_SOCIAL1 = "social1";
    public final static User TEST_USER1 = new User(
        TEST_USER_ID1,
        TEST_USERNAME1,
        TEST_PASSWORD1,
        TEST_EMAIL1,
        TEST_ROLE1,
        TEST_TYPE1,
        TEST_SOCIAL1
    );

    public final static Long TEST_USER_ID2 = 2L;
    public final static String TEST_USERNAME2 = "user2";
    public final static String TEST_EMAIL2 = "test2@test.com";
    public final static String TEST_PASSWORD2 = "password2";
    public final static UserRoleEnum TEST_ROLE2 = UserRoleEnum.USER;
    public final static SocialType TEST_TYPE2 = SocialType.KAKAO;
    public final static String TEST_SOCIAL2 = "social2";
    public final static User TEST_USER2 = new User(
        TEST_USER_ID2,
        TEST_USERNAME2,
        TEST_PASSWORD2,
        TEST_EMAIL2,
        TEST_ROLE2,
        TEST_TYPE2,
        TEST_SOCIAL2
    );

    public final static Long TEST_CATEGORY_ID = 1L;
    public final static String TEST_CATEGORY_NAME1 = "category1";
    public final static Category TEST_CATEGORY1 = new Category(
        TEST_CATEGORY_ID,
        TEST_CATEGORY_NAME1
    );

    public final static String TEST_CATEGORY_NAME2 = "category2";
    public final static Category TEST_CATEGORY2 = new Category(
        TEST_CATEGORY_ID,
        TEST_CATEGORY_NAME2
    );

    public final static Long TEST_ITEM_ID1 = 1L;
    public final static String TEST_ITEMNAME1 = "item name1";
    public final static Integer TEST_ITEMPRICE1 = 10000;
    public final static Integer TEST_BIDUNIT1 = 1000;
    public final static LocalDate TEST_DATE1 = LocalDate.parse("2027-04-19");
    public final static String TEST_URL1 = "test url1";
    public final static Integer TEST_BIDCOUNT = 0;
    public final static Integer TEST_VIEWCOUNT = 0;
    public final static ItemStatusEnum TEST_ITEMSTATUS_PENDING = ItemStatusEnum.PENDING;
    public final static ItemStatusEnum TEST_ITEMSTATUS_INPROGRESS = ItemStatusEnum.INPROGRESS;
    public final static ItemStatusEnum TEST_ITEMSTATUS_COMPLETED = ItemStatusEnum.COMPLETED;
    public final static Item TEST_ITEM1 = new Item(
        TEST_ITEM_ID1,
        TEST_ITEMNAME1,
        TEST_ITEMPRICE1,
        TEST_BIDUNIT1,
        TEST_BIDCOUNT,
        TEST_DATE1,
        TEST_URL1,
        TEST_ITEMPRICE1,
        TEST_VIEWCOUNT,
        TEST_CATEGORY1,
        TEST_USER1,
        TEST_ITEMSTATUS_PENDING
    );

    public final static String TEST_ITEMNAME2 = "item name2";
    public final static Integer TEST_ITEMPRICE2 = 20000;
    public final static Integer TEST_BIDUNIT2 = 2000;
    public final static LocalDate TEST_DATE2 = LocalDate.parse("2028-04-19");
    public final static String TEST_URL2 = "test url2";
    public final static Integer TEST_BIDCOUNT2 = 1;
    public final static Item TEST_ITEM2 = new Item(
        TEST_ITEM_ID1,
        TEST_ITEMNAME1,
        TEST_ITEMPRICE1,
        TEST_BIDUNIT1,
        TEST_BIDCOUNT,
        TEST_DATE1,
        TEST_URL1,
        TEST_ITEMPRICE1,
        TEST_VIEWCOUNT,
        TEST_CATEGORY1,
        TEST_USER1,
        TEST_ITEMSTATUS_INPROGRESS
    );

    public final static Item TEST_ITEM3 = new Item(
        TEST_ITEM_ID1,
        TEST_ITEMNAME1,
        TEST_ITEMPRICE1,
        TEST_BIDUNIT1,
        TEST_BIDCOUNT2,
        TEST_DATE1,
        TEST_URL1,
        TEST_ITEMPRICE1,
        TEST_VIEWCOUNT,
        TEST_CATEGORY1,
        TEST_USER1,
        TEST_ITEMSTATUS_COMPLETED
    );


    public final static Item TEST_ITEM4 = new Item(
        TEST_ITEM_ID1,
        TEST_ITEMNAME1,
        TEST_ITEMPRICE1,
        TEST_BIDUNIT1,
        TEST_BIDCOUNT,
        TEST_DATE1,
        TEST_URL1,
        TEST_ITEMPRICE1,
        TEST_VIEWCOUNT,
        TEST_CATEGORY1,
        TEST_USER1,
        TEST_ITEMSTATUS_COMPLETED
    );



}
