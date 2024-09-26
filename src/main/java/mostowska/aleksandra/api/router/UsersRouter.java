package mostowska.aleksandra.api.router;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mostowska.aleksandra.api.dto.ResponseDto;
import mostowska.aleksandra.model.dto.expense.CreateExpenseDto;
import mostowska.aleksandra.model.dto.income.CreateIncomeDto;
import mostowska.aleksandra.model.dto.investment.CreateInvestmentDto;
import mostowska.aleksandra.model.dto.savings_goal.CreateSavingGoalDto;
import mostowska.aleksandra.model.dto.user.CreateUserDto;
import mostowska.aleksandra.service.budget.ExpenseService;
import mostowska.aleksandra.service.budget.IncomeService;
import mostowska.aleksandra.service.budget.InvestmentService;
import mostowska.aleksandra.service.budget.SavingsGoalService;
import mostowska.aleksandra.service.user.UserService;
import org.springframework.stereotype.Component;
import spark.ResponseTransformer;

import java.math.BigDecimal;

import static spark.Spark.*;


/**
 * UsersRouter handles all routes related to user management, including user registration,
 * budgeting, expenses, incomes, investments, and savings goals while providing structured API responses.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UsersRouter {
    private final UserService userService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final InvestmentService investmentService;
    private final SavingsGoalService savingsGoalService;
    private final ResponseTransformer responseTransformer;
    private final Gson gson;

    public void routes() {

        path("/users", () -> {
            get("/test", (req, res) -> {
                log.info("GET /users/test called");
                return "Test route is working inside users!";
            });

            get(
                    "",
                    (request, response) -> {
                        Utils.setResponse(response, 200);
                        return new ResponseDto<>(userService.getAllUsers());
                    },
                    responseTransformer
            );

            get(
                    "/available_investments",
                    (request, response) -> {
                        Utils.setResponse(response, 200);
                        return new ResponseDto<>(investmentService.showAvailableInvestments());
                    },
                    responseTransformer
            );

            post(
                    "",
                    (request, response) -> {
                        log.info("INSIDE POST /USERS");
                        var createUserDto = gson.fromJson(request.body(), CreateUserDto.class);
                        Utils.setResponse(response, 201);
                        return new ResponseDto<>(userService.register(createUserDto));
                    },
                    responseTransformer
            );

            get(
                    "/activate",
                    (request, response) -> {
                        var id = Long.parseLong(request.queryParams("id"));
                        var timestamp = Long.parseLong(request.queryParams("timestamp"));
                        Utils.setResponse(response, 200);
                        return new ResponseDto<>(userService.activate(id, timestamp));
                    },
                    responseTransformer
            );

            path("/:id", () -> {
                        get(
                                "",
                                (request, response) -> {
                                    var userId = Long.parseLong(request.params(":id"));
                                    Utils.setResponse(response, 200);
                                    return new ResponseDto<>(userService.getUserById(userId));
                                },
                                responseTransformer
                        );

                        get(
                                "/budget",
                                (request, response) -> {
                                    var userId = Long.parseLong(request.params(":id"));
                                    Utils.setResponse(response, 200);
                                    return new ResponseDto<>(userService.getBudget(userId));
                                },
                                responseTransformer
                        );

                        get(
                                "/budget_after_goals",
                                (request, response) -> {
                                    var userId = Long.parseLong(request.params(":id"));
                                    Utils.setResponse(response, 200);
                                    return new ResponseDto<>(userService.getBudgetAfterGoals(userId));
                                },
                                responseTransformer
                        );

                        path("/expenses", () -> {
                                    get(
                                            "",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                Utils.setResponse(response, 200);
                                                return new ResponseDto<>(expenseService.getExpensesByUserId(userId));
                                            },
                                            responseTransformer
                                    );
                                get(
                                        "total",
                                        (request, response) -> {
                                            var userId = Long.parseLong(request.params(":id"));
                                            Utils.setResponse(response, 200);
                                            return new ResponseDto<>(expenseService.sumUsersExpenses(userId));
                                        },
                                        responseTransformer
                                );
                                    post(
                                            "",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                var createExpenseDto = gson.fromJson(request.body(),
                                                        CreateExpenseDto.class);
                                                Utils.setResponse(response, 201);
                                                return new ResponseDto<>(expenseService.addExpense(createExpenseDto, userId));
                                            },
                                            responseTransformer
                                    );
                                    delete(
                                            "/:expenseId",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                var expenseId = Long.parseLong(request.params(":expenseId"));
                                                Utils.setResponse(response, 200);
                                                return new ResponseDto<>(expenseService.removeExpense(expenseId, userId));
                                            },
                                            responseTransformer
                                    );
                                }
                        );

                        path("/incomes", () -> {
                                    get(
                                            "",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                Utils.setResponse(response, 200);
                                                return new ResponseDto<>(incomeService.getIncomesByUserId(userId));
                                            },
                                            responseTransformer
                                    );
                                get(
                                        "total",
                                        (request, response) -> {
                                            var userId = Long.parseLong(request.params(":id"));
                                            Utils.setResponse(response, 200);
                                            return new ResponseDto<>(incomeService.sumUsersIncomes(userId));
                                        },
                                        responseTransformer
                                );
                                    post(
                                            "",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                var createIncomeDto = gson.fromJson(request.body(),
                                                        CreateIncomeDto.class);
                                                Utils.setResponse(response, 201);
                                                return new ResponseDto<>(incomeService.addIncome(createIncomeDto, userId));
                                            },
                                            responseTransformer
                                    );
                                    delete(
                                            "/:incomeId",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                var incomeId = Long.parseLong(request.params(":incomeId"));
                                                Utils.setResponse(response, 200);
                                                return new ResponseDto<>(incomeService.removeIncome(incomeId, userId));
                                            },
                                            responseTransformer
                                    );
                                }
                        );

                        path("/investments", () -> {
                                    get(
                                            "",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                Utils.setResponse(response, 200);
                                                return new ResponseDto<>(investmentService.getInvestmentsByUserId(userId));
                                            },
                                            responseTransformer
                                    );
                                    get(
                                            "total",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                Utils.setResponse(response, 200);
                                                return new ResponseDto<>(investmentService.sumUsersInvestments(userId));
                                            },
                                            responseTransformer
                                    );
                                    post(
                                            "",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                var createInvestmentDto = gson.fromJson(request.body(),
                                                        CreateInvestmentDto.class);
                                                Utils.setResponse(response, 201);
                                                return new ResponseDto<>(investmentService.addInvestment(createInvestmentDto, userId));
                                            },
                                            responseTransformer
                                    );
                                    delete(
                                            "/:investmentId",
                                            (request, response) -> {
                                                var userId = Long.parseLong(request.params(":id"));
                                                var investmentId = Long.parseLong(request.params(":incomeId"));
                                                Utils.setResponse(response, 200);
                                                return new ResponseDto<>(investmentService.removeInvestment(investmentId, userId));
                                            },
                                            responseTransformer
                                    );
                                }
                        );

                path("/goals", () -> {
                            get(
                                    "",
                                    (request, response) -> {
                                        var userId = Long.parseLong(request.params(":id"));
                                        Utils.setResponse(response, 200);
                                        return new ResponseDto<>(savingsGoalService.getSavingGoalsByUserId(userId));
                                    },
                                    responseTransformer
                            );
                            get(
                                    "/total",
                                    (request, response) -> {
                                        var userId = Long.parseLong(request.params(":id"));
                                        Utils.setResponse(response, 200);
                                        return new ResponseDto<>(savingsGoalService.sumTotalSavingsGoalsAmount(userId));
                                    },
                                    responseTransformer
                            );
                            get(
                                    "/:goalId/pursue_date",
                                    (request, response) -> {
                                        var userId = Long.parseLong(request.params(":id"));
                                        var goalId = Long.parseLong(request.params(":goalId"));
                                        Utils.setResponse(response, 200);
                                        return new ResponseDto<>(savingsGoalService
                                                .getDateToPursueChosenGoal(userId, goalId, BigDecimal.valueOf(1000)));
                                    },
                                    responseTransformer
                            );
                            post(
                                    "",
                                    (request, response) -> {
                                        var userId = Long.parseLong(request.params(":id"));
                                        var createGoalDto = gson.fromJson(request.body(),
                                                CreateSavingGoalDto.class);
                                        Utils.setResponse(response, 201);
                                        return new ResponseDto<>(savingsGoalService.addSavingGoal(createGoalDto, userId));
                                    },
                                    responseTransformer
                            );
                            delete(
                                    "/:goalId",
                                    (request, response) -> {
                                        var userId = Long.parseLong(request.params(":id"));
                                        var goalId = Long.parseLong(request.params(":goalId"));
                                        Utils.setResponse(response, 200);
                                        return new ResponseDto<>(savingsGoalService.removeSavingGoal(goalId, userId));
                                    },
                                    responseTransformer
                            );
                        }
                );
                    }

            );

        });

        exception(RuntimeException.class, (ex, request, response) -> {
            var exceptionMessage = ex.getMessage();
            System.out.println("EX: " + exceptionMessage);
            response.redirect("/error?msg=" + exceptionMessage, 301);
        });

        path("/error", () ->
                get(
                        "",
                        (request, response) -> {
                            Utils.setResponse(response, 500);
                            var message = request.queryParams("msg");
                            return new ResponseDto<>(message);
                        },
                        responseTransformer
                )
        );

        internalServerError((request, response) -> {
            response.header("Content-Type", "application/json;charset=utf-8");
            return gson.toJson(new ResponseDto<>("Internal Server Error"));
        });

        notFound((request, response) -> {
            response.header("Content-Type", "application/json;charset=utf-8");
            return gson.toJson(new ResponseDto<>("Not found"));
        });
    }
}
