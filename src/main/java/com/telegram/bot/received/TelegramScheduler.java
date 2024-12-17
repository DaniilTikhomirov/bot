package com.telegram.bot.received;

import com.telegram.bot.models.TelegramUser;
import com.telegram.bot.models.Volunteers;
import com.telegram.bot.services.TelegramUsersService;
import com.telegram.bot.services.VolunteersService;
import com.telegram.bot.telegram_utils.MessageProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class TelegramScheduler {


    private final MessageProvider messageProvider;
    private final TelegramUsers telegramUsers;
    private final TelegramUsersService telegramUsersService;
    private final VolunteersService volunteersService;

    public TelegramScheduler(MessageProvider messageProvider, TelegramUsers telegramUsers, TelegramUsersService telegramUsersService, VolunteersService volunteersService) {
        this.messageProvider = messageProvider;
        this.telegramUsers = telegramUsers;
        this.telegramUsersService = telegramUsersService;
        this.volunteersService = volunteersService;
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void telegramScheduler() {
        List<Volunteers> volunteers = volunteersService.getAllVolunteers().stream().toList();
        volunteers.parallelStream().forEach(volunteer -> {
            volunteer.getUsers().forEach(telegramUser -> {
                messageProvider.PutMessage(telegramUser.getTelegramId(), "Отправьте заявку для подтверждения" +
                        "состояния животного! Заявка должна состоять в формате");
            });
        });
    }


}
