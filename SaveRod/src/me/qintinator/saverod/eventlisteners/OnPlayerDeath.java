package me.qintinator.saverod.eventlisteners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.qintinator.saverod.contracts.IConfigPropertyService;
import me.qintinator.saverod.contracts.ISaverodService;
import me.qintinator.saverod.statics.SaverodMessages;


public class OnPlayerDeath implements Listener {
	
	private final ISaverodService saverodService;
	private final IConfigPropertyService configPropertyService;
	
	public OnPlayerDeath(ISaverodService saverodService, IConfigPropertyService configPropertyService) {
		this.saverodService = saverodService;
		this.configPropertyService = configPropertyService;
	}
	
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		
		
		Player player = e.getEntity();		
		ItemStack offHandItem = player.getInventory().getItemInOffHand();
		
		if(offHandItem == null)
			return;
		
		if(player.getInventory().getItemInOffHand().isSimilar(saverodService.getSaveRod()))
		{

			
			if(configPropertyService.keepLevels()) {
				e.setDroppedExp(0);
				e.setKeepInventory(true);
			}
			
			if(offHandItem.getAmount() > 1)
				player.getInventory().getItemInOffHand().setAmount(offHandItem.getAmount() -1);		
			else
				player.getInventory().getItemInOffHand().setType(Material.AIR);		
			
			player.sendMessage(SaverodMessages.itemsSaved);		
			return;
		}
		
		
		for(int i = 0; i <  player.getInventory().getContents().length; i++) {
			
			ItemStack item = player.getInventory().getItem(i);
			
			
			if(item == null)
				continue;
			
			if(!item.isSimilar(saverodService.getSaveRod()))
				continue;
			
			e.setKeepInventory(true);
			
			if(configPropertyService.keepLevels()) {
				e.setKeepLevel(true);
				e.setDroppedExp(0);
			}
				
			if(item.getAmount() > 1)
				item.setAmount(item.getAmount() -1);
			else	
			player.getInventory().setItem(i, null);		
			
			player.sendMessage(SaverodMessages.itemsSaved);
			return;
		}
		
	}

}
