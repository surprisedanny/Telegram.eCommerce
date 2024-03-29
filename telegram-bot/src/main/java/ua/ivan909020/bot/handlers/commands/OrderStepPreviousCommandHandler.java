package ua.ivan909020.bot.handlers.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ua.ivan909020.bot.handlers.CommandHandler;
import ua.ivan909020.bot.handlers.UpdateHandler;
import ua.ivan909020.bot.handlers.commands.registries.CommandHandlerRegistry;
import ua.ivan909020.bot.models.domain.Button;
import ua.ivan909020.bot.models.domain.Command;
import ua.ivan909020.bot.repositories.ClientCommandStateRepository;

public class OrderStepPreviousCommandHandler implements UpdateHandler {

    private final CommandHandlerRegistry commandHandlerRegistry;
    private final ClientCommandStateRepository clientCommandStateRepository;

    public OrderStepPreviousCommandHandler(
            CommandHandlerRegistry commandHandlerRegistry,
            ClientCommandStateRepository clientCommandStateRepository) {

        this.commandHandlerRegistry = commandHandlerRegistry;
        this.clientCommandStateRepository = clientCommandStateRepository;
    }

    @Override
    public Command getCommand() {
        return Command.ORDER_STEP_PREVIOUS;
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        return update.hasMessage() &&
                update.getMessage().hasText() &&
                update.getMessage().getText().startsWith(Button.ORDER_STEP_PREVIOUS.getAlias());
    }

    @Override
    public void handleUpdate(AbsSender absSender, Update update) throws TelegramApiException {
        Long chatId = update.getMessage().getChatId();

        executePreviousCommand(absSender, update, chatId);
    }

    private void executePreviousCommand(AbsSender absSender, Update update, Long chatId) throws TelegramApiException {
        Command command = clientCommandStateRepository.popByChatId(chatId);
        if (command == null) {
            return;
        }

        CommandHandler commandHandler = commandHandlerRegistry.find(command);
        commandHandler.executeCommand(absSender, update, chatId);
    }

}
