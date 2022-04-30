package net.horizonsend.ion.core

import net.starlegacy.database.schema.misc.SLPlayer
import net.starlegacy.database.schema.nations.Nation
import net.starlegacy.database.schema.nations.Settlement
import net.starlegacy.database.slPlayerId
import net.starlegacy.util.vaultPermission
import org.bukkit.Statistic.PLAY_ONE_MINUTE
import org.bukkit.entity.Player

fun Player.updateProtection(): Boolean {
	// If protection has been voided
	if (hasPermission("ion.core.protection.void")) {
		vaultPermission.playerRemove(this, "suffix.1000. &6★ &r")
		return false
	}

	var hasProtection = true

	if (getStatistic(PLAY_ONE_MINUTE) / 72000 >= 96) hasProtection = false // If player has more than 96 hours of playtime
	if (SLPlayer[this].level >= 10) hasProtection = false // If the player is more than level 10

	// If they own a nation
	if (SLPlayer[this].nation != null)
		if (Settlement.findById(Nation.findById(SLPlayer[this].nation!!)!!.capital)!!.leader == slPlayerId)
			hasProtection = false

	if (!hasProtection)
		vaultPermission.playerAdd(this, "ion.core.protection.void")
	else
		vaultPermission.playerAdd(this, "suffix.1000. &6★ &r")

	return hasProtection
}