package ru.danmax.app.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.danmax.app.entity.Client;
import ru.danmax.app.entity.Role;
import ru.danmax.app.entity.Shop;
import ru.danmax.app.repository.ClientRepository;
import ru.danmax.app.repository.RoleRepository;
import ru.danmax.app.repository.ShopRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class ShopAdminsService {

    ShopRepository shopRepository;
    ClientRepository clientRepository;
    RoleRepository roleRepository;

    public List<Client> getShopAdmins(Long shopId) throws Exception {
        if (shopId == null) {
            throw new Exception("Shop id cannot be empty");
        }

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new Exception("Shop not found"));

        return shop.getAdmins();
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long shopId, List<Long> clients) throws Exception {
        if (shopId == null) {
            throw new Exception("Shop id cannot be empty");
        }

        if (clients == null || clients.isEmpty()){
            throw new Exception("List of admins cannot be empty");
        }

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new Exception("Shop not found"));

        // Снимаем всех старых администраторов
        for (Client lastAdmin : shop.getAdmins()){
            List<Role> newRoles = new ArrayList<>();
            for (Role role : lastAdmin.getRoles()){
                if (! Objects.equals(role.getName(), "SHOP_ADMIN")){
                    newRoles.add(role);
                }
            }
            lastAdmin.setRoles(new HashSet<>(newRoles));
            lastAdmin.setShop(null);
            clientRepository.save(lastAdmin);
        }

        // Добавляем новых администраторов
        for (Long clientId : clients) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new Exception("Client with id = " + clientId + " not found"));
            Set<Role> roles = client.getRoles();
            Role shop_admin = roleRepository.findByName("SHOP_ADMIN");

            if (roles.contains(shop_admin)) {
                throw new Exception("Client with id = " + clientId + " is already admin of another shop");
            }

            roles.add(shop_admin);
            client.setRoles(roles);
            client.setShop(shop);
            clientRepository.save(client);
        }
    }

    public boolean isClientShopAdmin(Long clientId, Long shopId) throws Exception {
        if (clientId == null){
            throw new Exception("ClientId cannot be empty");
        }

        if (shopId == null){
            throw new Exception("ShopId cannot be empty");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new Exception("User not found"));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new Exception("Shop not found"));

        // Проверка на то, что в ролях есть роль "SHOP_ADMIN"
        boolean roleFlag = false;
        Role shop_admin = roleRepository.findByName("SHOP_ADMIN");
        for (Role role : client.getRoles()){
            if (role.equals(shop_admin)) {
                roleFlag = true;
                break;
            }
        }
        if (!roleFlag){
            return false;
        }

        // Проверка на то, что магазин у пользователя соответствует запрашиваемому магазину
        return (client.getShop() != null && shop.equals(client.getShop()));
    }
}
