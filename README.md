# Kafka Streams Proof of Concept

## Producers

### Generate Commercial Order

- Random member -> create new member or use exisitng
- Random order lines number (1 to 10)
- Random products -> create new product or use existing one
- Random price for the new products (1 to 100)
- Publish CommercialOrder in a "commercial-orders" topic
- Publish New members in "members" topic
- Publish New products in "products" topic

## Kafka Streams

### Split the commercial order lines

- From commercial-orders
- Join products
- To commercial-order-lines

### Generate purchase orders

- From commercial-order-lines
- To purchase-orders

### Send orders to warehouse

- From commercial-orders
- Join members
- Join products
- To warehouse-orders

### Generate the bill

- From commercial-orders
- To bills

## Topics

- commercial-orders
- commercial-order-lines
- members
- products
- warehouse-orders
- purchase-orders
- bills

## Schemas

### CommercialOrder

#### CommercialOrder

- **uuid**: string
- **datetime**: long
- **memberUuid**: string
- **shippingAddress**: _CommercialOrderAddress_
- **billingAddress**: _CommercialOrderAddress_, nullable, default null.
- **lines**: array of _CommercialOrderLine_

#### CommercialOrderLine

- **uuid**: string
- **productUuid**: string
- **price**: float
- **quantity**: int, default 1

#### CommercialOrderAddress

- **country**: string
- **state**: string, nullable, default null
- **city**: string
- **zipCode**: string
- **street**: string, nullable, default null
- **number**: string, nullable, default null
- **extra**: string, nullable, default null
