import axios from 'axios'
export const api=axios.create({baseURL:'/api/v1',headers:{'Content-Type':'application/json'}})
api.interceptors.response.use(r=>r,error=>Promise.reject(new Error(error.response?.data?.message??'Não foi possível concluir a operação.')))
