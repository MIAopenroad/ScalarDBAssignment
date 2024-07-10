export interface Costomer {
	customerId: string
	password: string
    email: String
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
	orderId: string
	customerId: string
    timestamp: string
    status: boolean
}

export interface Statement {
	statementId: string
    orderId: string
	itemId: string
    count: number
}

export interface OrderWithStatements extends Order{
    statements: Statement[]
}

export interface ExtendedStatement {
	statementId: string
    orderId: string
	itemId: string
    count: number
    item: Item
}

export interface OrderWithExtendedStatements extends Order{
    extendedStatements: ExtendedStatement[]
}