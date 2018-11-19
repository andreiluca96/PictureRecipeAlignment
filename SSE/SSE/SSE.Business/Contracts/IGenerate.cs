namespace SSE.Business.Contracts
{
    public interface IGenerate<in T>
    {
        void Generate(T entity);
    }
}
