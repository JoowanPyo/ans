package com.gemiso.zodiac.core.enumeration;

import java.util.List;

public enum FixAuth {

    REPORTER("reporter"),
    EDITOR("editor"),
    ANCHOR("anchor"),
    DESK("desk"),
    PD("pd"),
    ADMIN("admin");

    private static Long Reporter_id = 6L;
    private static Long Editor_id = 7L;
    private static Long Anchor_id = 8L;
    private static Long PD_id = 10L;
    private static Long Desk_id = 9L;
    private static Long Admin_id = 11L;

    public FixAuth FixUserGroup(Long ID){
        FixAuth ret = REPORTER;
        // DB tb_user_grp 에 해당하는 ID값 수동으로 매핑
        // mh 2021-10-08 jw와 같이 작업 함
        // hk 지시사항 : 수동으로 처리할 수 잇는 부분은 코드에서 처리해도 된다.

        if(ID == Editor_id)
            ret = EDITOR;
        else if(ID == Anchor_id)
            ret = ANCHOR;
        else if(ID == Desk_id)
            ret = DESK;
        else if(ID == PD_id)
            ret = PD;
        else if(ID == Admin_id)
            ret = ADMIN;

        return ret;
    }

    private String position;

    FixAuth(String grpNm) {
        this.position = grpNm;

    }

    public static FixAuth FixMaxAuth(List<Long> authList){

        // authList = new ArrayList<FixAuth>();

        if(authList.contains(Admin_id) )
            return ADMIN;

        if(authList.contains(Desk_id) )
            return DESK;

        if(authList.contains(Anchor_id) )
            return ANCHOR;

        //if(authList.contains(PD_id) )
            //return PD;

        if(authList.contains(Editor_id) )
            return EDITOR;

        return REPORTER;
    }

}
