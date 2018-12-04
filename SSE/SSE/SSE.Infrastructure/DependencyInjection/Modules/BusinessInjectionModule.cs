using Autofac;
using SSE.Business;

namespace SSE.Infrastructure.Modules
{
    public class BusinessInjectionModule : Module
    {
        protected override void Load(ContainerBuilder builder)
        {
            var businessAssembly = typeof(BusinessLayer).Assembly;
            builder.RegisterAssemblyTypes(businessAssembly);
        }
    }
}
