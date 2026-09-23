export const money=(value:number,currency='BRL')=>new Intl.NumberFormat('pt-BR',{style:'currency',currency,minimumFractionDigits:2}).format(value)
export const percent=(value:number)=>new Intl.NumberFormat('pt-BR',{style:'percent',minimumFractionDigits:2,maximumFractionDigits:4}).format(value)
export const dateTime=(value:string)=>new Intl.DateTimeFormat('pt-BR',{dateStyle:'short',timeStyle:'short'}).format(new Date(value))
export const today=()=>new Date().toISOString().slice(0,10)
export const futureDate=(days=30)=>{const date=new Date();date.setDate(date.getDate()+days);return date.toISOString().slice(0,10)}
