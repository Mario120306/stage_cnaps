export class Mere{
    
    protected readonly resourcePlural:string;
    protected readonly resourceSingular:string;
    protected readonly baseUrl:string;

    constructor(
        resourcePural:string,
        resourceSingular:string,
        baseUrl:string
    ){
        this.resourcePlural=resourcePural;
        this.resourceSingular=resourceSingular;
        this.baseUrl=baseUrl;
    }
    protected async request<T>(endpoint:string,options:RequestInit={}):Promise<T>{
        const url=`${this.baseUrl}/${endpoint.replace(/^\/+/, "")}`;
        const res= await fetch(url,{
            ...options,
            headers:{
                Accept:"application/json",
                ...(options.body ? { 'Content-Type': 'application/json' } : {}),
                ...(options.headers || {}),
            },
        });
        if(!res.ok){
            let detailErreur="";
            try{
                const corpsErreur=await res.clone().json();
                detailErreur =corpsErreur?.error?.message || JSON.stringify(corpsErreur);
            }catch{
                detailErreur=await res.text();
            }
            throw new Error(`Erreur API :${res.status}- ${detailErreur}`);
        }
        return res.json();
    }
    async getById<T>(id:string):Promise<T>{
        return this.request<T>(`${this.resourcePlural}/${id}`);
    }
    async create<T>(data:Partial<T>):Promise<T>{
        return this.request<T>(this.resourcePlural,{
            method:"POST",
            body:JSON.stringify(data),
        });
    }
    async update<T>(id:string,data:Partial<T>):Promise<T>{
        return this.request<T>(`${this.resourcePlural}/${id}`,{
            method:"PUT",
            body:JSON.stringify(data)
        })
    }
    async delete(id:string):Promise<void>{
        await this.request<void>(`${this.resourcePlural}/${id}`,{
            method:"DELETE",
        })
    }
    /**
     * getAll optionally accepts query parameters (object) that will be serialized into the query string.
     * Example: getAll({ page: 0, size: 5, titre: 'abc' })
     */
    async getAll<T>(params?: Record<string, string | number | boolean>):Promise<T[]>{
        let endpoint = this.resourcePlural;
        if (params && Object.keys(params).length > 0) {
            const esc = encodeURIComponent;
            const query = Object.entries(params)
                .filter(([_, v]) => v !== undefined && v !== null && String(v) !== "")
                .map(([k, v]) => `${esc(k)}=${esc(String(v))}`)
                .join("&");
            if (query) endpoint += `?${query}`;
        }
        return this.request<T[]>(endpoint);
    }
}