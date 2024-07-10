export interface Costomer {
	CustomerId: string
	Password: string
    Email: String
}

export interface Item {
	itemId: string
	name: string
    price: number
    stock: number
    description: string
    imageUrl: string
}

export interface Order {
	OrderId: string
	CustomerId: string
    Timestamp: string
    Status: boolean
}

export interface Statement {
	StatementId: string
    OrderId: string
	ItemId: string
    Count: number
}

export interface OrderWithStatements extends Order{
    Statements: Statement[]
}