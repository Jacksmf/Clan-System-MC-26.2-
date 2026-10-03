package com.untistore.clans.services;

import com.untistore.clans.models.ClanPlayer;
import com.untistore.clans.models.Role;

public class MemberService {

    public boolean promoteMember(ClanPlayer player, ClanPlayer promoter) {
        if (!promoter.getRole().isAtLeast(Role.MANAGER)) {
            return false;
        }

        Role nextRole =  player.getRole().getNext();
        if (nextRole == null) {
            return false;
        }

        player.setRole(nextRole);
        return true;
    }
}
