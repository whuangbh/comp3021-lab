package lab6;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;



public class Account {
	public int id;
	public int balance;

	public Account(int id, int balance) {
		this.id = id;
		this.balance = balance;
	}

	// TODO: Task1
	// replace the null with a lambda expression
	public static Consumer<Account> add100 = ( (Account a) -> {a.balance += 100;} );



	// TODO: Task2
	// define checkBound using lowerBound and upperBound
	// We want checkBound to check BOTH lowerBound AND upperBound.
	public static Predicate<Account> lowerBound = a -> a.balance >=0;
	public static Predicate<Account> upperBound = a -> a.balance <=10000;
	public static Predicate<Account> checkBound = ( (Account a) -> {return lowerBound.test(a) && upperBound.test(a);} );

	interface AddMaker {
		Consumer<Account> make(int N);
	}

	// TODO: Task3
	// replace the null with a lambda expression
	public static AddMaker maker = ( (int n) -> {
												 return ( (Account a) -> {a.balance += n;} )
												 ;} );


	// You can assume that all the Account in accounts have positive balances.
	public static int getMaxAccountID(List<Account> accounts) {
		// TODO: Task4
		// replace the null with a lambda expression
		Account maxOne = accounts.stream().reduce(new Account(0, -100),
												  (Account a, Account b) -> {
												      return (a.balance > b.balance)? a : b;
												  }
												 );

		return maxOne.id;
	}


}
