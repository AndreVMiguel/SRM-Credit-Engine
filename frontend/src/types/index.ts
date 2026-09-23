export type ReceivableType='MERCANTILE_DUPLICATE'|'POSTDATED_CHECK'
export interface PricingRequest {faceValue:number;dueDate:string;receivableType:ReceivableType;assetCurrency:string;settlementCurrency:string;baseRateMonthly:number}
export interface PricingResponse {faceValue:number;presentValue:number;netAmount:number;baseRateMonthly:number;spreadMonthly:number;exchangeRate:number;termDays:number}
export interface SettlementRequest {idempotencyKey:string;assignorId:string;pricing:PricingRequest}
export interface Settlement {id:string;assignorId:string;receivableType:ReceivableType;faceValue:number;assetCurrency:string;settlementCurrency:string;dueDate:string;netAmount:number;status:string;createdAt:string}
export interface Page<T>{content:T[];totalElements:number;totalPages:number;number:number;size:number;first:boolean;last:boolean}
export interface StatementFilters {from:string;to:string;currency:string;receivableType:''|ReceivableType;page:number;size:number;sort:string}
