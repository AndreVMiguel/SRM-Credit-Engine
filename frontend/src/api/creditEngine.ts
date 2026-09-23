import {api} from './client'
import type {Page,PricingRequest,PricingResponse,Settlement,SettlementRequest,StatementFilters} from '../types'
export const simulatePricing=async(payload:PricingRequest)=>(await api.post<PricingResponse>('/pricing/simulations',payload)).data
export const createSettlement=async(payload:SettlementRequest)=>(await api.post<Settlement>('/settlements',payload)).data
export const getSettlements=async(filters:StatementFilters)=>{const params=new URLSearchParams({page:String(filters.page),size:String(filters.size),sort:filters.sort});if(filters.from)params.set('from',new Date(`${filters.from}T00:00:00Z`).toISOString());if(filters.to){const end=new Date(`${filters.to}T00:00:00Z`);end.setUTCDate(end.getUTCDate()+1);params.set('to',end.toISOString())}if(filters.currency)params.set('currency',filters.currency);if(filters.receivableType)params.set('receivableType',filters.receivableType);return (await api.get<Page<Settlement>>(`/settlements?${params}`)).data}
