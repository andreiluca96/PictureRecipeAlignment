using Autofac;

namespace SSE.Infrastructure.Modules
{
    internal class CommonInjectionModule : Module
    {
        protected override void Load(ContainerBuilder builder)
        {
            builder.RegisterType<AutofacDependencyScope>()
                .As<IDependencyScope>()
                .InstancePerLifetimeScope();
        }
    }
}